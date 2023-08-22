package com.qiniu.qplayer2.ui.widget.commonplayer.controlwidget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.qiniu.qmedia.component.player.QIPlayerAudioListener
import com.qiniu.qmedia.component.player.QIPlayerStateChangeListener
import com.qiniu.qmedia.component.player.QPlayerState
import com.qiniu.qplayer2.R
import com.qiniu.qplayer2.ui.page.longvideo.LongLogicProvider
import com.qiniu.qplayer2.ui.page.longvideo.LongPlayableParams
import com.qiniu.qplayer2.ui.page.longvideo.LongVideoParams
import com.qiniu.qplayer2ext.commonplayer.CommonPlayerCore
import com.qiniu.qplayer2ext.commonplayer.layer.control.IControlWidget

class CommonPlayerMuteWidget : AppCompatImageView, View.OnClickListener,
    IControlWidget<LongLogicProvider, LongPlayableParams, LongVideoParams>, QIPlayerAudioListener {

    private lateinit var mPlayerCore: CommonPlayerCore<LongLogicProvider, LongPlayableParams, LongVideoParams>

    private val mPlayerStateChangeListener = object: QIPlayerStateChangeListener {
        override fun onStateChanged(state: QPlayerState) {
            if (state == QPlayerState.PLAYING) {
                visibility = View.VISIBLE
            } else if (state == QPlayerState.PREPARE) {
                visibility = View.INVISIBLE
            }
        }

    }

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onMuteChanged(is_mute: Boolean) {
        updateIcon(is_mute)
    }

    private fun updateIcon(is_mute: Boolean) {
        if (is_mute) {
            setImageResource(R.drawable.qmedia_ic_volume_mute)
        } else {
            setImageResource(R.drawable.qmedia_ic_volume_unmute)
        }
    }

    override fun onClick(v: View?) {
        mPlayerCore.mPlayerContext.getPlayerControlHandler()
            .setMute(!mPlayerCore.mPlayerContext.getPlayerControlHandler().isMute)
    }

    override fun onWidgetActive() {
//        setImageResource(R.drawable.qmedia_player_play_pause_level_list)
        setOnClickListener(this)

        updateIcon(mPlayerCore.mPlayerContext.getPlayerControlHandler().isMute)

        if (mPlayerCore.mPlayerContext.getPlayerControlHandler().currentPlayerState == QPlayerState.PLAYING) {
            visibility = View.VISIBLE
        } else {
            visibility = View.INVISIBLE
        }

        mPlayerCore.mPlayerContext.getPlayerControlHandler().also {
            it.addPlayerAudioListener(this)
            it.addPlayerStateChangeListener(mPlayerStateChangeListener)
        }




    }

    override fun onWidgetInactive() {
        setOnClickListener(null)

        mPlayerCore.mPlayerContext.getPlayerControlHandler().also {
            it.removePlayerAudioListener(this)
            it.removePlayerStateChangeListener(mPlayerStateChangeListener)
        }
    }

    override fun bindPlayerCore(playerCore: CommonPlayerCore<LongLogicProvider, LongPlayableParams, LongVideoParams>) {
        mPlayerCore = playerCore
    }
}