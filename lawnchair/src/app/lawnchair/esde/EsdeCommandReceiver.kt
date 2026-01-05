package app.lawnchair.esde

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log

class EsdeCommandReceiver : BroadcastReceiver() {

    /*TODO ...maybe:
    -add Steamgriddb scraper? (save to the es-media folder or maybe an override folder. Both?)
    -do steamgriddb images even look okay on these aspect ratios
    -add options for file path selection (es-de media folder, es-de scroll txt folder)
    -Add option to set logo anchor? Maybe even absolute position but that probably sucks with all the different sizes
    -Save state when screensaver starts? How reliable is the screensaver end indicator? Does it send a game-selected if game is picked or saver is cancelled?
    -Potentially allow for movement or zooming out on background on a per game override basis? There's quite a few fanart images that just don't look good when you center crop
    Could store it in a separate file and read out on startup, adjust on the fly when that game is selected. The UI required for this sounds the most annoying/complicated
    */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_SET_GAME_SELECTED) {
            val gameSelected = intent.getStringExtra(EXTRA_GAME_SELECTED)
            val system = intent.getStringExtra(EXTRA_SYSTEM)
            EsdeController.setGameState(EsdeService.processGameSelected(gameSelected, system))
        }
        else if (intent.action == ACTION_SET_SYSTEM_SELECTED) {
            val system = intent.getStringExtra(EXTRA_SYSTEM)
            EsdeController.setSystemState(EsdeService.getSystem(system))
        }
    }

    companion object {
        const val ACTION_SET_GAME_SELECTED =
            "app.lawnchair.action.SET_GAME_SELECTED"
        const val ACTION_SET_SYSTEM_SELECTED =
            "app.lawnchair.action.SET_SYSTEM_SELECTED"

        const val EXTRA_GAME_SELECTED = "game_selected"
        const val EXTRA_SYSTEM = "system"
    }
}
