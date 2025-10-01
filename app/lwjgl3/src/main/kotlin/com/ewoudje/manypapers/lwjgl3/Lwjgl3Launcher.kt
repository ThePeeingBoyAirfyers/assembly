@file:JvmName("Lwjgl3Launcher")

package com.ewoudje.manypapers.lwjgl3

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import com.ewoudje.assembly.Game

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return

    //RenderDoc.setCaptureOption(RenderDoc.CaptureOption.API_VALIDATION, true)
    //RenderDoc.setCaptureOption(RenderDoc.CaptureOption.VERIFY_BUFFER_WRITES, true)
    //RenderDoc.enableOverlayOptions(RenderDoc.OverlayOption.ALL)
    Lwjgl3Application(Game {
        val g = Gdx.graphics as Lwjgl3Graphics
        val m = g.monitors[1]
        //g.window.setPosition(m.virtualX, m.virtualY)
        //g.window.maximizeWindow()
        g.setResizable(false)
        g.setWindowedMode(384 * 4, 216 * 4)
        g.window.setVisible(true)
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    } as ApplicationListener, Lwjgl3ApplicationConfiguration().apply {
        setTitle("ManyPapers")
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        useVsync(true)
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1)
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.

        //For debugging
        setPauseWhenLostFocus(false)
        setPauseWhenMinimized(false)
        setInitialVisible(false)

        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        //// They can also be loaded from the root of assets/ .
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })

}
