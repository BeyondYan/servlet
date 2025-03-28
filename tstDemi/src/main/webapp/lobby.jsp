<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>德州扑克游戏大厅</title>
  <link rel="stylesheet" href="styles.css">
  <script>
    // 页面加载时，直接从session中获取ID和balance信息
    window.onload = function() {
      let sessionID = 'null';
      let balance = 'null';
      
      // 显示ID和余额
      document.getElementById("user-id").textContent = sessionID;
      document.getElementById("user-balance").textContent = balance;
    }

    
    document.getElementById("room-list").addEventListener("click", function(event) {
        if (event.target.tagName === "BUTTON" && event.target.dataset.room) {
            enterRoom(event.target.dataset.room);
        }
    });

    // 进入房间的逻辑
    function enterRoom(roomNumber) {
        console.log("进入房间:", roomNumber);

        // 将房间编号存入 sessionStorage（浏览器端存储）
        sessionStorage.setItem("selectedRoom", roomNumber);

        // 跳转到房间详情页（roomInfo.jsp）
        window.location.href = "roomInfo.jsp";  
    }

    // 获取房间列表并渲染
    function fetchRoomList() {
        fetch("/tstDemi/roomList")
            .then(response => response.json())
            .then(data => {
                let roomContainer = document.getElementById("room-list");
                roomContainer.innerHTML = '';  // 清空现有内容

                if (data.rooms && data.rooms.length > 0) {
                    document.getElementById("lobby").style.display = "none";
                    document.getElementById("room-list-container").style.display = "block";

                    data.rooms.forEach(room => {
                        let roomNumber = room.number ? room.number : "未知";
                        let currentCount = room.count ? room.count : 0;
                        let maxCount = 6;
                        let playerInfo = `${currentCount}/${maxCount}`;
                        let buttonState = (parseInt(room.isAccessible) === 1) ? '' : 'disabled';
                        let buttonText = (parseInt(room.isAccessible) === 1) ? '加入房间' : '已满';

                        // 构建房间元素，按钮带 `data-room` 属性
                        let roomElement = `<div class="room">
                            ${roomNumber}::${playerInfo}
                            <button data-room="${room.number}" ${buttonState}>${buttonText}</button>
                        </div>`;

                        roomContainer.innerHTML += roomElement;
                    });
                } else {
                    console.log("没有房间信息");
                }
            })
            .catch(error => {
                console.log("获取房间列表失败:", error);
            });
    }

    // 返回大厅
    function backToLobby() {
        // 隐藏房间信息并显示大厅
        document.getElementById("room-list-container").style.display = "none";
        document.getElementById("lobby").style.display = "block";
    }
  </script>
</head>
<body>

  <!-- 大厅主页 -->
  <div id="lobby">
    <h1>欢迎来到游戏大厅</h1>
    <p>玩家 ID: <span id="user-id"></span></p>
    <p>余额: <span id="user-balance"></span></p>
    <button id="exit-btn" onclick="backToLobby()">退出游戏</button>
    <button id="room-mode-btn" onclick="fetchRoomList()">房间模式</button>
    <div id="image-container"></div> <!-- 预加载的图片会显示在这里 -->
  </div>

  <!-- 房间列表 -->
  <div id="room-list-container" style="display: none;">
    <h2>请选择房间...</h2>
    <div id="room-list">
      <!-- 房间信息将通过 JavaScript 动态插入 -->
    </div>
    <button id="back-to-lobby-btn" onclick="backToLobby()">返回大厅</button>
  </div>

  <!-- 房间信息页面 -->
  <div id="room-info" style="display: none;">
    <h1>房间信息</h1>
    <p>房间号: <span id="room-id"></span></p>
    <p>人数: <span id="room-players"></span></p>
    <button id="start-game-btn">开始游戏</button>
    <button id="back-btn" onclick="backToRoomList()">返回房间列表</button>
  </div>

</body>
</html>




