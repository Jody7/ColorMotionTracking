import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.*;

public class Hwnd {

    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class,
                W32APIOptions.DEFAULT_OPTIONS);
        HWND FindWindow(String lpClassName, String lpWindowName);
        int GetWindowRect(HWND handle, int[] rect);
    }

    public static int[] getRect(String windowName) throws WindowNotFoundException,
            GetWindowRectException {
        HWND hwnd = User32.INSTANCE.FindWindow(null, windowName);
        if (hwnd == null) {
            throw new WindowNotFoundException("", windowName);
        }

        int[] rect = {0, 0, 0, 0};
        int result = User32.INSTANCE.GetWindowRect(hwnd, rect);
        if (result == 0) {
            throw new GetWindowRectException(windowName);
        }
        return rect;
    }
    public static class WindowNotFoundException extends Exception {
        public WindowNotFoundException(String className, String windowName) {
            super(String.format("Null Window",
                    className, windowName));
        }
    }

    public static class GetWindowRectException extends Exception {
        public GetWindowRectException(String windowName) {
            super("Window NOT Found");
        }
    }

    public static int[] WindowDimension(String window) {
        int[] rect;
        try {
            rect = Hwnd.getRect(window);
            return rect;
        } catch (Hwnd.WindowNotFoundException e) {
            e.printStackTrace();
        } catch (Hwnd.GetWindowRectException e) {
            e.printStackTrace();
        }

        return null;

    }
}
