package cn.mcplus.websocket.packages;

public class AuthenticationBody extends BasicBody {
    String secretKey;

    public AuthenticationBody(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
