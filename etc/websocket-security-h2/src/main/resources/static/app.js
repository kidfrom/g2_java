document.getElementById("connect").addEventListener("click", connect);
document.getElementById("disconnect").addEventListener("click", disconnect);
document.getElementById("sendChannelMessage").addEventListener("click", sendChannelMessage);
document.getElementById("sendPrivateMessage").addEventListener("click", sendPrivateMessage);

var stompClient = null;
var channelId = null;

function view(connected) {
  document.getElementById("connect").disabled = connected;
  document.getElementById("disconnect").disabled = !connected;
  document.getElementById("sendChannelMessage").disabled = !connected;
  document.getElementById("sendPrivateMessage").disabled = !connected;
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function connect(e) {
  e.preventDefault();

  channelId = document.getElementById("channelId").value;

  if (channelId) {
    const stompConfig = {
      brokerURL: "ws://localhost:8080/chat",
      debug: function (str) {
        console.log('STOMP: ' + str);
      },
      connectHeaders: {"X-XSRF-TOKEN": getCookie("XSRF-TOKEN")},
      onConnect: () => {
        const destinations = ["/topic/channel/1", "/user/queue/messages"];

        console.log('stompClient subscribe');
        for (let i = 0; i < destinations.length; i++) {
          stompClient.subscribe(destinations[i], (payload) => {
            var message = JSON.parse(payload.body);

            var table = document.getElementById("messages");
            var row = table.insertRow();
            var cell1 = row.insertCell();
            var cell2 = row.insertCell();
            var cell3 = row.insertCell();
            var cell4 = row.insertCell();
            cell1.innerHTML = destinations[i];
            cell2.innerHTML = message.userId;
            cell3.innerHTML = message.text;
            cell4.innerHTML = message.iat;
          });
        }
      },
    }

    stompClient = new StompJs.Client(stompConfig);

    stompClient.activate();

    view(true);
  }
}

function disconnect() {
  stompClient.disconnect();
  view(false);
  console.log("Disconnected");
}

function sendChannelMessage(e) {
  e.preventDefault();

  var text = document.getElementById("channelMessageText").value;

  var messageModel = { text };

  stompClient.publish({destination: `/app/channel/${channelId}`, body: JSON.stringify(messageModel)});
  document.getElementById("channelMessageText").value = "";
}

function sendPrivateMessage(e) {
  e.preventDefault();

  var toUser = document.getElementById("toUser").value;
  var text = document.getElementById("privateMessageText").value;

  var messageModel = { text };

  stompClient.publish({destination: `/app/user/${toUser}`, body: JSON.stringify(messageModel)});
  document.getElementById("privateMessageText").value = "";
}