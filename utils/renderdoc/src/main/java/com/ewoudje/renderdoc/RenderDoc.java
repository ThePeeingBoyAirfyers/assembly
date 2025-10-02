package com.ewoudje.renderdoc;
//Copied from https://github.com/wisp-forest/owo-lib/blob/1.21.6/src/main/java/io/wispforest/owo/renderdoc

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

import java.time.Instant;
import java.util.EnumSet;
import java.util.logging.Logger;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class RenderDoc {

    private static final RenderdocLibrary.RenderdocApi renderdoc;
    private static final Logger LOGGER = Logger.getLogger("RenderDoc");

    static {
        var apiPointer = new PointerByReference();
        RenderdocLibrary.RenderdocApi apiInstance = null;


        try {
            RenderdocLibrary renderdocLibrary;

            //TODO export dll

            renderdocLibrary = Native.load("renderdoc", RenderdocLibrary.class);
/*
            if (true) { // WINDOWS
                renderdocLibrary = Native.load("renderdoc", RenderdocLibrary.class);
            } else {
                int flags = DynamicLinkLoader.RTLD_NOW | DynamicLinkLoader.RTLD_NOLOAD;
                if (DynamicLinkLoader.dlopen("librenderdoc.so", flags) == 0) {
                    throw new UnsatisfiedLinkError();
                }

                renderdocLibrary = Native.load("renderdoc", RenderdocLibrary.class, Map.of(Library.OPTION_OPEN_FLAGS, flags));

            }
 */

            int initResult = renderdocLibrary.RENDERDOC_GetAPI(10500, apiPointer);
            if (initResult != 1) {
                LOGGER.warning("Could not connect to RenderDoc API, return code: " + initResult);
            } else {
                apiInstance = new RenderdocLibrary.RenderdocApi(apiPointer.getValue());

                var major = new IntByReference();
                var minor = new IntByReference();
                var patch = new IntByReference();
                apiInstance.GetAPIVersion.call(major, minor, patch);
                LOGGER.info(() -> "Connected to RenderDoc API v" + major.getValue() + "." + minor.getValue() + "." + patch.getValue());
            }
        } catch (UnsatisfiedLinkError e) {
            LOGGER.warning("Failed to link renderdoc");
        }


        renderdoc = apiInstance;
    }

    private RenderDoc() {
    }

    /**
     * @return {@code true} if the RenderDoc dynamic library is loaded
     * and owo has successfully connected to the API
     */
    public static boolean isAvailable() {
        return renderdoc != null;
    }

    /**
     * @return The version of the RenderDoc API that owo is connected to,
     * in &lt;major&gt;.&lt;minor&gt;.&lt;patch&gt; semver format
     */
    public static String getAPIVersion() {
        if (renderdoc == null) return "not connected";

        var major = new IntByReference();
        var minor = new IntByReference();
        var patch = new IntByReference();
        renderdoc.GetAPIVersion.call(major, minor, patch);

        return major.getValue() + "." + minor.getValue() + "." + patch.getValue();
    }

    /**
     * Set the value of a RenderDoc capture option
     *
     * @param option The option to modify
     * @param value  The value to change the option to
     * @return {@code true} if the value was correct and the option
     * was successfully modified
     */
    public static <T> boolean setCaptureOption(CaptureOption<T> option, T value) {
        if (renderdoc == null) return false;

        if (value instanceof Boolean bool) {
            return renderdoc.SetCaptureOptionU32.call(option.idx, new RenderdocLibrary.uint32_t(bool ? 1 : 0)) == 1;
        } else if (value instanceof Integer uint) {
            return renderdoc.SetCaptureOptionU32.call(option.idx, new RenderdocLibrary.uint32_t(uint)) == 1;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Get the value of a RenderDoc capture option
     *
     * @param option The option to query
     * @return The current value of the option
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCaptureOption(CaptureOption<T> option) {
        if (renderdoc == null) return null;

        if (option.type == Boolean.class) {
            return (T) Boolean.valueOf(renderdoc.GetCaptureOptionU32.call(option.idx).intValue() == 1);
        } else if (option.type == Integer.class) {
            return (T) Integer.valueOf(renderdoc.GetCaptureOptionU32.call(option.idx).intValue());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Set the hotkeys used to trigger a capture
     */
    public static void setCaptureKeys(int... keys) {
        if (renderdoc == null) return;
        renderdoc.SetCaptureKeys.call(keys, keys.length);
    }

    /**
     * Query the current configuration of the RenderDoc overlay
     *
     * @return All parts of the overlay which are currently enabled
     */
    public static EnumSet<OverlayOption> getOverlayOptions() {
        if (renderdoc == null) return null;

        int mask = renderdoc.GetOverlayBits.call().intValue();

        var set = EnumSet.noneOf(OverlayOption.class);
        for (var option : OverlayOption.values()) {
            if ((mask & option.mask) != 0) set.add(option);
        }

        return set;
    }

    /**
     * Enable some parts of the RenderDoc overlay
     *
     * @param options The options to enable
     */
    public static void enableOverlayOptions(OverlayOption... options) {
        if (renderdoc == null) return;

        int mask = 0;
        for (var option : options) mask |= option.mask;

        renderdoc.MaskOverlayBits.call(new RenderdocLibrary.uint32_t(~0), new RenderdocLibrary.uint32_t(mask));
    }

    /**
     * Disable some parts of the RenderDoc overlay
     *
     * @param options The options to enable
     */
    public static void disableOverlayOptions(OverlayOption... options) {
        if (renderdoc == null) return;

        int mask = 0;
        for (var option : options) mask |= option.mask;

        renderdoc.MaskOverlayBits.call(new RenderdocLibrary.uint32_t(~mask), new RenderdocLibrary.uint32_t(0));
    }

    /**
     * Try to remove all RenderDoc hooks from the process. If this
     * is called after a graphics API has been initialized, behavior
     * is undefined
     */
    public static void removeHooks() {
        if (renderdoc == null) return;
        renderdoc.RemoveHooks.call();
    }

    /**
     * Remove RenderDoc's crash handler from the process
     */
    public static void unloadCrashHandler() {
        if (renderdoc == null) return;
        renderdoc.UnloadCrashHandler.call();
    }

    /**
     * @return the template used to generate new capture file names
     */
    public static String getCaptureFilePathTemplate() {
        if (renderdoc == null) return null;
        return renderdoc.GetCaptureFilePathTemplate.call();
    }

    /**
     * Set the template used to generate new capture file names
     */
    public static void setCaptureFilePathTemplate(String template) {
        if (renderdoc == null) return;
        renderdoc.SetCaptureFilePathTemplate.call(template);
    }

    /**
     * Query information about a specific capture
     *
     * @param index The index to query
     * @return The path and timestamp of the capture at the given index,
     * or {@code null} if no such capture exists
     */
    public static Capture getCapture(int index) {
        if (renderdoc == null) return null;

        var length = new IntByReference();
        if (renderdoc.GetCapture.call(index, null, length, null).intValue() != 1) {
            return null;
        }

        var filename = new byte[length.getValue()];
        var timestamp = new LongByReference();

        renderdoc.GetCapture.call(index, filename, length, timestamp);
        return new Capture(new String(filename, 0, filename.length - 1), Instant.ofEpochSecond(timestamp.getValue()));
    }

    /**
     * @return How many captures have been made
     */
    public static int getNumCaptures() {
        if (renderdoc == null) return -1;
        return renderdoc.GetNumCaptures.call().intValue();
    }

    /**
     * Trigger a capture of the next frame, as
     * if the user had pressed on the capture hotkeys
     */
    public static void triggerCapture() {
        if (renderdoc == null) return;
        renderdoc.TriggerCapture.call();
    }

    /**
     * Immediately begin a capture
     */
    public static void startFrameCapture() {
        if (renderdoc == null) return;
        renderdoc.StartFrameCapture.call(null, null);
    }

    /**
     * @return {@code true} if a capture is currently being performed
     */
    public static boolean isFrameCapturing() {
        if (renderdoc == null) return false;
        return renderdoc.IsFrameCapturing.call().intValue() == 1;
    }

    /**
     * Immediately end an active capture
     */
    public static void endFrameCapture() {
        if (renderdoc == null) return;
        renderdoc.EndFrameCapture.call(null, null);
    }

    /**
     * @return {@code true} if a RenderDoc replay UI
     * instance is currently attached to this process
     */
    public static boolean isReplayUIConnected() {
        if (renderdoc == null) return false;
        return renderdoc.IsTargetControlConnected.call().intValue() == 1;
    }

    /**
     * Open the RenderDoc replay UI
     *
     * @param connect {@code true} if the new UI instance should instantly
     *                attach to this process
     * @return The PID of the spawned process, or {@code 0} if the UI could not be opened
     */
    public static int launchReplayUI(boolean connect) {
        if (renderdoc == null) return -1;
        return renderdoc.LaunchReplayUI.call(new RenderdocLibrary.uint32_t(connect ? 1 : 0), null).intValue();
    }

    /**
     * Request the currently connected replay UI to raise
     * its window to the top - this is not guaranteed to work on every OS
     *
     * @return {@code true} if the UI tried to raise its window, {@code false}
     * if some error occurred while passing on the command or no UI is connected
     */
    public static boolean showReplayUI() {
        if (renderdoc == null) return false;
        return renderdoc.ShowReplayUI.call().intValue() == 1;
    }

    /**
     * Set the comments attached to a specific capture
     *
     * @param capture  The capture to modify, obtain with {@link #getCapture(int)}
     * @param comments The new capture comments
     */
    public static void setCaptureComments(Capture capture, String comments) {
        if (renderdoc == null) return;
        renderdoc.SetCaptureFileComments.call(capture.path, comments);
    }

    public enum OverlayOption {
        ENABLED(0x1),
        FRAME_RATE(0x2),
        FRAME_NUMBER(0x4),
        CAPTURE_LIST(0x8),
        DEFAULT(ENABLED.mask | FRAME_RATE.mask | FRAME_NUMBER.mask | CAPTURE_LIST.mask),
        ALL(~0),
        NONE(0);

        public final int mask;

        OverlayOption(int mask) {
            this.mask = mask;
        }
    }

    public static final class CaptureOption<T> {
        public static final CaptureOption<Boolean> ALLOW_VSYNC = new CaptureOption<>(0, Boolean.class);
        public static final CaptureOption<Boolean> ALLOW_FULLSCREEN = new CaptureOption<>(1, Boolean.class);
        public static final CaptureOption<Boolean> API_VALIDATION = new CaptureOption<>(2, Boolean.class);
        public static final CaptureOption<Boolean> CAPTURE_CALLSTACKS = new CaptureOption<>(3, Boolean.class);
        public static final CaptureOption<Boolean> CAPTURE_CALLSTACKS_ONLY_DRAWS = new CaptureOption<>(4, Boolean.class);
        public static final CaptureOption<Integer> DELAY_FOR_DEBUGGER = new CaptureOption<>(5, Integer.class);
        public static final CaptureOption<Boolean> VERIFY_BUFFER_WRITES = new CaptureOption<>(6, Boolean.class);
        public static final CaptureOption<Boolean> HOOK_INTO_CHILDREN = new CaptureOption<>(7, Boolean.class);
        public static final CaptureOption<Boolean> REF_ALL_RESOURCES = new CaptureOption<>(8, Boolean.class);
        public static final CaptureOption<Boolean> SAVE_ALL_INITIALS = new CaptureOption<>(9, Boolean.class);
        public static final CaptureOption<Boolean> CAPTURE_ALL_CMD_LISTS = new CaptureOption<>(10, Boolean.class);
        public static final CaptureOption<Boolean> DEBUG_OUTPUT_MUTE = new CaptureOption<>(11, Boolean.class);

        @Deprecated
        public static final CaptureOption<?> ALLOW_UNSUPPORTED_VENDOR_EXTENSIONS = new CaptureOption<>(12, Void.class);

        public final int idx;
        private final Class<T> type;

        CaptureOption(int idx, Class<T> type) {
            this.idx = idx;
            this.type = type;
        }
    }

    public record Capture(String path, Instant timestamp) {
    }
}
