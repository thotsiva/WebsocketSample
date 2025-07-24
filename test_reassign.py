import uuid
import time
from unittest.mock import patch
from logger.log_manager import LogManager
from opentelemetry.trace import set_span_in_context
import atexit


def run_validator_steps(parent_ctx, common_kwargs):
    with LogManager.elf.start_span("validator_steps", ctx=parent_ctx) as span:
        ctx = set_span_in_context(span)
        LogManager.info(
            parent_context=ctx,
            **common_kwargs,
            step="Reassign Validator",
            log_message="Validator started successfully"
        )

        time.sleep(1)  # Reduced for testing

        LogManager.info(
            parent_context=ctx,
            **common_kwargs,
            step="Reassign Validator",
            log_message="Validator ended successfully"
        )


@patch(target="logger.log_manager.get_env", return_value="default")
@patch("logger.log_manager.get_config")
def test_reassign_flow(mock_get_config, mock_get_env):
    # Mock config data
    mock_get_config.return_value = {
        "pii_fields": []
    }

    LogManager.initialize()

    correlation_id = str(uuid.uuid4())
    common_kwargs = {
        "correlationId": correlation_id,
        "user": "elf_testing",
        "event": "reassignConversationEvent"
    }

    with LogManager.elf.start_span("reassign_conversation_steps") as parent_span:
        parent_ctx = set_span_in_context(parent_span)

        LogManager.info(
            parent_context=parent_ctx,
            **common_kwargs,
            step="Reassign Flow",
            log_message="Reassign flow started"
        )

        run_validator_steps(parent_ctx, common_kwargs)

        LogManager.info(
            parent_context=parent_ctx,
            **common_kwargs,
            step="Reassign Flow",
            log_message="Reassign flow completed"
        )


def shutdown_tracing():
    """Safely shutdown tracing and logging processors"""
    try:
        tracer_provider = LogManager.elf.tracer._tracer_provider
        if hasattr(tracer_provider, "shutdown"):
            tracer_provider.shutdown()

        if hasattr(LogManager.elf.logger_provider, "shutdown"):
            LogManager.elf.logger_provider.shutdown()

    except Exception as e:
        print(f"Error during shutdown: {e}")


# Register safe shutdown
atexit.register(shutdown_tracing)


if __name__ == "__main__":
    test_reassign_flow()
    print("✅ Test complete. Check logs/spans.")
