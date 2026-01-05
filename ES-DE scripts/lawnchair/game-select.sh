escape_for_su() {
  printf '%s' "$1" | sed 's/"/\\"/g'
}

GAME="$(escape_for_su "$(basename "$1")")"
SYSTEM="$(escape_for_su "$3")"

su -c "am broadcast \
  -n app.lawnchair.play.debug/app.lawnchair.esde.EsdeCommandReceiver \
  -a app.lawnchair.action.SET_GAME_SELECTED \
  --es game_selected \"$GAME\" \
  --es system \"$SYSTEM\""
