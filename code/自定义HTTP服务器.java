package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 根据uri内容不同
 * 返回不同的信息
 * 404  找不到内容
 * 200返回“你好”
 */
public class MyHTTP {
    private static final int port = 9003;

    public static void main(String[] args) throws IOException {
        //创建服务器
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务器已启动！");
        //等待客户端连接
        Socket socket = serverSocket.accept();
        //构建读写对象
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            //从客户端拿信息
            //得到首行信息
            String firstLine = reader.readLine();//只读取一行
            String[] firstLineArr = firstLine.split(" ");//按空格分隔成数组
            //method
            String method = firstLineArr[0];
            //uri
            String uri = firstLineArr[1];
            //HTTPVersion
            String version = firstLineArr[2];
            //输出首行信息
            System.out.println(String.format("首行信息->方法：%s，URI：%s，HTTP版本号：%s", method, uri, version));

            //创建返回内容
            String content = "";
            if (uri.contains("404")) {
                content = "<h1>页面未找到</h1>";
            } else if (uri.contains("200")) {
                content = "<h1>你好，龙 gie gie</h1>";
            }
            //内容输出
            //首行信息
            writer.write(String.format("%s 200 ok", version) + "\n");
            //head
            writer.write("Content-Type:text/html;charset=utf-8\n");
            writer.write("Content-Length:" + content.getBytes().length + "\n");
            //空行
            writer.write("\n");
            writer.write(content);
            writer.flush();
        }
    }
}
