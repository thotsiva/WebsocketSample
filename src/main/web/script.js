let socket;

function connectWebSocket() {
  const url = document.getElementById('ws-url').value;
  if (!url) {
    alert('Please enter a WebSocket URL');
    return;
  }

  // Close existing WebSocket if it's open
  if (socket && socket.readyState === WebSocket.OPEN) {
    socket.close();
  }

  // Create a new WebSocket connection
  socket = new WebSocket(url);

  socket.onopen = () => {
    document.getElementById('ws-log').innerText = 'Connected to WebSocket!';
  };

  socket.onmessage = (event) => {
    const log = document.getElementById('ws-log');
    log.innerText += `\nReceived: ${event.data}`;
  };

  socket.onerror = (error) => {
    document.getElementById('ws-log').innerText = `Error: ${error.message}`;
  };

  socket.onclose = () => {
    document.getElementById('ws-log').innerText += '\nConnection closed.';
  };
}

function sendMessage() {
  const message = document.getElementById('ws-message').value;
  if (socket && socket.readyState === WebSocket.OPEN) {
    socket.send(message);
    document.getElementById('ws-log').innerText += `\nSent: ${message}`;
  } else {
    alert('Please connect to WebSocket first.');
  }
}