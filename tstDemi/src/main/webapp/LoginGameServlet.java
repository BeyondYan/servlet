import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;

@WebServlet("/loginGame")
public class LoginGameServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    // 远程服务器的地址和端口
    private static final String SERVER_HOST = "120.26.58.228";
    private static final int SERVER_PORT = 17015;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应类型为 HTML
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 连接到远程服务器
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter outToServer = null;

        try {
            // 创建 socket 并连接到远程服务器
            socket = new Socket(SERVER_HOST, SERVER_PORT);

            // 输入输出流设置
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new PrintWriter(socket.getOutputStream(), true);

            // 这里可以向服务器发送数据（如果需要）
            // outToServer.println("Some data to send to the server");

            // 读取并显示来自服务器的响应
            String serverResponse = in.readLine();  // 读取一行数据
            if (serverResponse != null) {
                out.println("<h1>收到服务器响应：</h1>");
                out.println("<p>" + serverResponse + "</p>");
            } else {
                out.println("<h1>没有收到有效的服务器响应。</h1>");
            }

            // 如果需要继续读取更多响应，可以在这里添加逻辑
            // 比如使用一个循环持续读取服务器响应

        } catch (IOException e) {
            out.println("<h1>连接远程服务器失败！</h1>");
            e.printStackTrace();
        }
    }
}

