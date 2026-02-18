package org.stypox.dicio.skills.notify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.SkillOutput
import org.stypox.dicio.R
import org.stypox.dicio.io.graphical.Headline
import org.stypox.dicio.util.StringUtils
import org.stypox.dicio.util.getString

data class NotifyOutput(val notifications: List<Notification>): SkillOutput {
    override fun getSpeechOutput(ctx: SkillContext): String {
        val notificationsStrings = notifications.map { notification ->
            val message = StringUtils.joinNonBlank(
                notification.title,
                notification.message,
                separator = ctx.getString(R.string.skill_notify_messages_separator)
            )
            if (notification.appName.isEmpty()) {
                ctx.getString(R.string.skill_notify_message_no_app, message)
            } else {
                ctx.getString(R.string.skill_notify_message_app, notification.appName, message)
            }
        }
        return if (notificationsStrings.isEmpty()) {
            ctx.getString(R.string.skill_notify_no_notifications)
        } else {
            notificationsStrings.joinToString(" ")
        }
    }

    @Composable
    override fun GraphicalOutput(ctx: SkillContext) {
        if (notifications.isEmpty()) {
            Headline(stringResource(R.string.skill_notify_no_notifications))
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (notification in notifications) {
                    NotificationBox(notification)
                }
            }
        }
    }
}

@Composable
private fun NotificationBox(notification: Notification) {
    Column {
        if (notification.appName.isNotBlank() || notification.title.isNotBlank()) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(notification.appName)
                    }
                    if (notification.appName.isNotBlank() && notification.title.isNotBlank()) {
                        append(StringUtils.DEFAULT_SEPARATOR)
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(notification.title)
                    }
                },
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Text(
            text = notification.message,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
