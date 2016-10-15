package com.popalay.spotty.utils.ui.changehandlers

import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandlerCompat

class ArcFadeMoveChangeHandlerCompat : TransitionChangeHandlerCompat(ArcFadeMoveChangeHandler(), FadeChangeHandler())