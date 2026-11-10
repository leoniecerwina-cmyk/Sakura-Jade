package de.sakurajade.workflow;

import android.content.Context;
import android.content.SharedPreferences;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "WidgetBridge")
public class WidgetBridge extends Plugin {
    @PluginMethod
    public void update(PluginCall call) {
        String json = call.getString("json", "{}");
        Context c = getContext();
        c.getSharedPreferences("sakura_widget", Context.MODE_PRIVATE).edit().putString("data", json).apply();
        SakuraWidget.refreshAll(c);
        call.resolve(new JSObject());
    }
}
