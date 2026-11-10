package de.sakurajade.workflow;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import org.json.JSONObject;

public class SakuraWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context ctx, AppWidgetManager mgr, int[] ids) {
        for (int id : ids) mgr.updateAppWidget(id, build(ctx));
    }

    static void refreshAll(Context ctx) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(ctx);
        int[] ids = mgr.getAppWidgetIds(new ComponentName(ctx, SakuraWidget.class));
        for (int id : ids) mgr.updateAppWidget(id, build(ctx));
    }

    static RemoteViews build(Context ctx) {
        RemoteViews v = new RemoteViews(ctx.getPackageName(), R.layout.widget_sakura);
        String line1 = "Noch keine Daten – App einmal öffnen";
        String line2 = "";
        String line3 = "";
        try {
            String raw = ctx.getSharedPreferences("sakura_widget", Context.MODE_PRIVATE).getString("data", null);
            if (raw != null) {
                JSONObject o = new JSONObject(raw);
                int streak = o.optInt("streak", 0);
                int study = o.optInt("studyMin", 0);
                int goal = o.optInt("goalMin", 0);
                String next = o.optString("next", "");
                int tasks = o.optInt("tasks", 0);
                line1 = "🔥 " + streak + " Tage   ·   📚 " + study + (goal > 0 ? " / " + goal : "") + " Min";
                line2 = next.isEmpty() ? "Kein Termin in den nächsten Tagen" : "📅 " + next;
                line3 = "✅ " + tasks + " offene Aufgaben";
            }
        } catch (Exception ignored) { }
        v.setTextViewText(R.id.w_l1, line1);
        v.setTextViewText(R.id.w_l2, line2);
        v.setTextViewText(R.id.w_l3, line3);
        Intent i = new Intent(ctx, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, i, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        v.setOnClickPendingIntent(R.id.w_root, pi);
        return v;
    }
}
