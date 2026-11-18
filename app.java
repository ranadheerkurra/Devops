import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class EcommerceApp {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        // Binding to 0.0.0.0 allows access via your server's Public IP
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        
        server.createContext("/", new StoreHandler());
        server.setExecutor(null);
        System.out.println("E-Commerce Server started on port " + port);
        server.start();
    }

    static class StoreHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Product> catalog = new ArrayList<>();
            catalog.add(new Product(101, "DevOps Handbook", 29.99));
            catalog.add(new Product(102, "Mechanical Keyboard", 89.50));
            catalog.add(new Product(103, "Cloud Server Mug", 14.00));

            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><title>DevOps Store</title>");
            html.append("<style>body{font-family:Arial,sans-serif;margin:40px;background:#f4f4f9;}");
            html.append(".card{background:white;padding:15px;margin:10px 0;border-radius:5px;box-shadow:0 2px 4px rgba(0,0,0,0.1);}</style>");
            html.append("</head><body>");
            html.append("<h1>Welcome to the DevOps Mini-Store (v1.0)</h1>");
            html.append("<p>Server Status: <strong>ONLINE</strong></p><hr>");

            for (Product p : catalog) {
                html.append("<div class='card'>");
                html.append("<h3>").append(p.getName()).append("</h3>");
                html.append("<p>Product ID: ").append(p.getId()).append(" | Price: $").append(p.getPrice()).append("</p>");
                html.append("</div>");
            }

            html.append("</body></html>");

            byte[] response = html.toString().getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}

