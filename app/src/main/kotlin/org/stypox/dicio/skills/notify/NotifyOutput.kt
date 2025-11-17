package org.stypox.dicio.skills.notify

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.SkillOutput
import org.stypox.dicio.R
import org.stypox.dicio.util.getString

data class NotifyOutput(val notifications: List<Notification>): SkillOutput {
    override fun getSpeechOutput(ctx: SkillContext): String {
        var response = ""
        if (notifications.isNotEmpty()) {
            notifications.forEach { notification ->
                response += if (notification.appName != null) {
                    ctx.getString(R.string.skill_notify_message, notification.appName, notification.message ?: "")
                } else {
                    ctx.getString(R.string.skill_notify_unknown_app, notification.message ?: "")
                }
            }
        }
        else {
            response = ctx.getString(R.string.no_notifications)
        }
        return response.trim()
    }

    @Composable
    override fun GraphicalOutput(ctx: SkillContext) {
        if (notifications.isNotEmpty()) {
            Column {
                for (notification in notifications) {
                    if (notification.appName != null) {
                        Text(
                            text = notification.appName, // String? -> "null" if message is null
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Text(
                        text = notification.message.toString(),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}