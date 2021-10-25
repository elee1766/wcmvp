package gfx.wcmvp;

public class index {
  public static ConnectorWC connector;
  public static void main(String[] args) {
    connector = new ConnectorWC();
    connector.resetSession();
    String qrString = connector.getConfig().toWCUri();
    System.out.println(qrString);
  }
}
