import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.lock.Lock;
import com.couchbase.client.java.lock.LockTimeoutException;
import com.couchbase.client.java.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CouchbaseLockUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CouchbaseLockUtil.class);

    private final Bucket bucket; // Couchbase bucket to perform operations on

    public CouchbaseLockUtil(Bucket bucket) {
        this.bucket = bucket;
    }

    /**
     * Attempts to acquire a lock for a given batchId.
     *
     * @param batchId The unique identifier for the batch of documents to lock
     * @return True if the lock was successfully acquired, otherwise False
     */
    public boolean tryLockBatch(String batchId) {
        try {
            // Define the lock timeout (10 seconds in this case)
            long lockTimeout = 10L;
            // Create a unique document for the lock (with a TTL)
            JsonObject lockContent = JsonObject.create().put("locked", true).put("timestamp", System.currentTimeMillis());

            // Attempt to lock by inserting a document with a TTL that expires after the timeout
            JsonDocument lockDoc = JsonDocument.create(batchId, lockContent);
            lockDoc.expiry((int) lockTimeout);

            // Try to insert the lock document into Couchbase
            bucket.upsert(lockDoc);

            LOG.info("Successfully acquired lock for batch {}", batchId);
            return true;
        } catch (DocumentAlreadyExistsException e) {
            // Document already exists (lock is held by another process)
            LOG.info("Lock for batch {} is already held by another process.", batchId);
            return false;
        } catch (Exception e) {
            LOG.error("Error acquiring lock for batch {}: ", batchId, e);
            return false;
        }
    }

    /**
     * Releases the lock for a given batchId.
     *
     * @param batchId The identifier for the locked batch
     */
    public void releaseLock(String batchId, boolean success, String errorMessage) {
        try {
            if (success) {
                // If successful, remove the lock document
                bucket.remove(batchId);
                LOG.info("Successfully released lock for batch {}", batchId);
            } else {
                // If there was an error, log the failure but don't remove the lock
                LOG.error("Failed to process batch {}: {}", batchId, errorMessage);
            }
        } catch (Exception e) {
            LOG.error("Error releasing lock for batch {}: ", batchId, e);
        }
    }

    /**
     * Tries to acquire the lock with a specified timeout.
     * This is a more robust version where we specify the timeout for the lock request.
     *
     * @param batchId    The unique identifier for the batch of documents to lock
     * @param timeout    The timeout in seconds for waiting to acquire the lock
     * @return True if the lock was acquired, otherwise False
     */
    public boolean tryLockBatchWithTimeout(String batchId, long timeout) {
        try {
            long lockTimeout = timeout; // Timeout in seconds
            // Create the lock document with the timeout
            JsonObject lockContent = JsonObject.create().put("locked", true).put("timestamp", System.currentTimeMillis());
            JsonDocument lockDoc = JsonDocument.create(batchId, lockContent);
            lockDoc.expiry((int) lockTimeout);

            // Wait and try to acquire the lock with the timeout
            while (true) {
                try {
                    bucket.upsert(lockDoc);
                    LOG.info("Successfully acquired lock for batch {} within timeout", batchId);
                    return true;
                } catch (DocumentAlreadyExistsException e) {
                    // Wait for a short period before retrying
                    try {
                        Thread.sleep(500); // sleep for half a second
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error acquiring lock for batch {}: ", batchId, e);
            return false;
        }
    }

    /**
     * Check whether a batchId lock exists (i.e., is currently locked).
     *
     * @param batchId The batchId for which to check the lock
     * @return True if the batch is locked, otherwise False
     */
    public boolean isLocked(String batchId) {
        try {
            JsonDocument lockDoc = bucket.get(batchId);
            return lockDoc != null;
        } catch (Exception e) {
            LOG.error("Error checking lock for batch {}: ", batchId, e);
            return false;
        }
    }
}
