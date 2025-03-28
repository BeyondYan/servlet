import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RoomListServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取客户端的 session 信息
       // String sessionID = (String) request.getSession().getAttribute("ID");
        //String balance = (String) request.getSession().getAttribute("balance");

        // 设置响应内容类型
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // 建立TCP连接并发送请求
        try (Socket socket = new Socket("120.26.58.228", 17015);
             BufferedReader dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter dos = new PrintWriter(socket.getOutputStream(), true)) {
             
            // 读取服务器的确认信息
            dis.readLine();  // 忽略服务器的确认信息
            
            // 发送请求
            dos.println("r\n");

            // 读取服务器返回的房间信息
            String responseData = dis.readLine();  // 读取一行数据

            // 房间信息格式化成 JSON
            List<Map<String, String>> rooms = parseRoomData(responseData);

            // 创建 JSON 响应
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("{\"rooms\":[");

            for (int i = 0; i < rooms.size(); i++) {
                Map<String, String> room = rooms.get(i);
                jsonResponse.append("{\"number\":\"")
                            .append(room.get("number"))
                            .append("\",\"count\":\"")
                            .append(room.get("count"))
                            .append("\",\"isAccessible\":\"")
                            .append(room.get("isAccessible"))
                            .append("\"}");

                if (i < rooms.size() - 1) {
                    jsonResponse.append(",");
                }
            }

            jsonResponse.append("]}");

            // 返回 JSON 数据
            out.println(jsonResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

	private List<Map<String, String>> parseRoomData(String data) {
	    List<Map<String, String>> roomList = new ArrayList<>();
	    
	    System.out.println("接收到的房间数据：" + data);
	    // 假设房间数据格式是类似："roomnumber:101:0:1;roomnumber:102:0:1;..."
	    String[] rooms = data.split(";");

	    for (String roomData : rooms) {
	        if (!roomData.trim().isEmpty()) {
	            Map<String, String> room = new HashMap<>();
	            String[] roomDetails = roomData.split(":");

	            // 检查拆分后的数组长度是否为4
	            if (roomDetails.length == 4) {
	                // 从 roomDetails[1] 获取房间号，其他部分分别是人数和是否可进入
	                String roomNumber = roomDetails[1].trim();  // 房间号
	                String count = roomDetails[2].trim();       // 当前人数
	                String isAccessible = roomDetails[3].trim();  // 是否可进入

	                // 确保数据字段映射正确
	                room.put("number", roomNumber);  
	                room.put("count", count);  
	                room.put("isAccessible", isAccessible);  
	            } else {
	                // 如果数据格式不正确，输出错误信息
	                System.err.println("错误：房间数据格式不正确：" + roomData);
	                continue;
	            }

	            roomList.add(room);
	        }
	    }

	    return roomList;
	}




}


