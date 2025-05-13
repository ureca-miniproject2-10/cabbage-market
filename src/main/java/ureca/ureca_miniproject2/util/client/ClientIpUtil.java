package ureca.ureca_miniproject2.util.client;

import jakarta.servlet.http.HttpServletRequest;

public class ClientIpUtil {

    public static String getClientIp(HttpServletRequest request) {
        // 1. X-Forwarded-For (프록시 또는 로드 밸런서가 있는 경우)
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 다중 IP가 포함될 수 있으므로 가장 앞의 IP만 사용
            return ip.split(",")[0].trim();
        }

        // 2. Proxy-Client-IP (Apache)
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 3. WL-Proxy-Client-IP (WebLogic)
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 4. HTTP_CLIENT_IP (Proxy 서버의 경우)
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 5. HTTP_X_FORWARDED_FOR (Proxy 서버의 경우)
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 6. 직접 연결된 클라이언트 IP
        return request.getRemoteAddr();
    }
}

