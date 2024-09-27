// const stompClient = new StompJs.Client({
//     brokerURL: 'ws://localhost:8080/stomp-endpoint',
// });
//
// // Set up event handlers
// stompClient.onConnect = onStompConnect;
// stompClient.onWebSocketError = onWebSocketError;
// stompClient.onStompError = onStompError;
//
// // Function to handle Stomp connection
// function onStompConnect(frame) {
//     setConnected(true);
//     console.log('Connected: ' + frame);
//     // 구독
//     stompClient.subscribe('/chatting/topic/room/1', (greeting) => {
//         const content = greeting.body;
//         showChatlist(content);
//     });
// }
//
// // Function to handle WebSocket error
// function onWebSocketError(error) {
//     console.error('Error with WebSocket:', error);
// }
//
// // Function to handle Stomp error
// function onStompError(frame) {
//     console.error('Broker reported error: ' + frame.headers['message']);
//     console.error('Additional details: ' + frame.body);
// }
//
// // Function to set the connection status
// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     $("#conversation").toggle(connected);
//     $("#greetings").html("");
// }
//
// // Function to connect to the WebSocket
// function connect() {
//     stompClient.activate();
// }
//
// // Function to disconnect from the WebSocket
// function disconnect() {
//     stompClient.deactivate();
//     setConnected(false);
//     console.log("Disconnected");
// }
//
// // Function to send a message
// function sendChat() {
//     stompClient.publish({
//         destination: "/app/chat/123",
//         body: JSON.stringify({'message': $("#chat").val(), 'roomId': 1})
//     });
// }
//
// // Function to display a greeting message
// function showChatlist(message) {
//     try {
//         const parsedMessage = JSON.parse(message);
//         const content = parsedMessage.message;
//         console.log('content',content)
//         $("#chatlist").append($("<tr><td>" + content + "</td></tr>"));
//     } catch (error) {
//         console.error('Error parsing WebSocket message:', error);
//     }
// }
//
// // Set up event listeners
// $(function () {
//     $("form").on('submit', (e) => e.preventDefault());
//     $("#connect").click(() => connect());
//     $("#disconnect").click(() => disconnect());
//     $("#send").click(() => sendChat());
// });