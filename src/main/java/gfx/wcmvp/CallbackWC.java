package gfx.wcmvp;

import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.walletconnect.Session;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static gfx.wcmvp.index.connector;

public class CallbackWC implements Session.Callback {
  @Override
  public void onMethodCall(@NotNull Session.MethodCall methodCall) {
    System.out.println("got a method call: " + methodCall);
    long nonce = System.currentTimeMillis();
    List<String> accounts = connector.session.approvedAccounts();
    if (accounts != null) {
      if (accounts.size() > 0) {
        try {
          TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("attempting to sign message");
        connector.session.performMethodCall(
            new Session.MethodCall.Custom(
                nonce, "personal_sign", Arrays.asList("0xdeadbeef", accounts.get(0))),
            response -> {
              String signed = response.getResult().toString();
              System.out.println(signed);
              return null;
            });
        return;
      }
    }
    System.out.println("accounts null");
  }

  @Override
  public void onStatus(@NotNull Session.Status status) {
    System.out.println("got status: " + status.getClass().toString());
  }
}
