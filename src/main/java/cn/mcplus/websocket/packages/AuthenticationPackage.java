package cn.mcplus.websocket.packages;

public class AuthenticationPackage extends BasicPackage {
    public AuthenticationPackage(Header header, AuthenticationBody body) {
        this.header = header;
        this.body = body;
    }

    public AuthenticationBody getBody() {
        return body;
    }

    AuthenticationBody body;
}

