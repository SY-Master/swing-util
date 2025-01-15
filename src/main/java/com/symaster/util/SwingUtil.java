package com.symaster.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Swing 工具类
 *
 * @author yinmiao
 * @date 2022-01-25 18:41
 */
public class SwingUtil {

    public static final Map<String, ImageIcon> ICON_CACHE_MAP = new HashMap<>();

    public static void antialiasingOn(Graphics2D g2d) {
        if (g2d == null) {
            return;
        }
        // RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // RenderingHints.KEY_RENDERING 可以设置为 RenderingHints.VALUE_RENDER_QUALITY
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    public static CanvasImage createCanvasImage(int w, int h, int imageType) {
        CanvasImage canvasImage = new CanvasImage();

        canvasImage.setImage(new BufferedImage(w, h, imageType));
        canvasImage.setGraphics(canvasImage.getImage().getGraphics());
        antialiasingOn((Graphics2D) canvasImage.getGraphics());

        canvasImage.getGraphics().setColor(new Color(0, 0, 0, 0));
        canvasImage.getGraphics().drawRect(0, 0, w, h);
        return canvasImage;
    }

    public static CanvasImage createCanvasImage(BufferedImage image) {
        CanvasImage canvasImage = new CanvasImage();

        canvasImage.setImage(image);
        canvasImage.setGraphics(canvasImage.getImage().getGraphics());
        antialiasingOn((Graphics2D) canvasImage.getGraphics());
        canvasImage.getGraphics().setColor(new Color(0, 0, 0, 0));
        canvasImage.getGraphics().drawRect(0, 0, image.getWidth(), image.getHeight());
        return canvasImage;
    }

    public static void setAllBackground(JComponent root_panel, Color color, JComponent... ignored) {
        if (root_panel != null) {
            if (ignored != null) {
                for (JComponent jComponent : ignored) {
                    if (jComponent != null && jComponent == root_panel) {
                        return;
                    }
                }
            }
            root_panel.setBackground(color);
            Component[] components = root_panel.getComponents();
            if (components != null) {
                for (Component component : components) {
                    if (component instanceof JComponent) {
                        setAllBackground((JComponent) component, color, ignored);
                    }
                }
            }
        }
    }

    public static void setAllBackground(JComponent root_panel, Color color) {
        if (root_panel != null) {
            root_panel.setBackground(color);
            Component[] components = root_panel.getComponents();
            if (components != null) {
                for (Component component : components) {
                    if (component instanceof JComponent) {
                        setAllBackground((JComponent) component, color);
                    }
                }
            }
        }
    }

    public static void toCenter(Window component) {
        component.setLocationRelativeTo(null);
    }

    public static void openFolder(File folder) throws IOException {
        if (folder.isFile()) {
            folder = folder.getParentFile();
        }
        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new IOException("指定目录不存在, 并且无法创建");
        }
        Desktop.getDesktop().open(folder);
    }

    public static ImageIcon loadIcon(URL url) {
        return new ImageIcon(url);
    }

    public static ImageIcon loadIcon(URL url, int w, int h) {
        return new ImageIcon(loadIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    /**
     * 从项目资源目录"icon"加载图片
     */
    public static ImageIcon loadIcon(String iconName) {
        return new ImageIcon(Objects.requireNonNull(SwingUtil.class.getResource("/icon/" + iconName)));
    }

    /**
     * 从项目资源目录"icon"加载图片
     * <p>
     * 如果宽度或高度为负数，则替换一个值以保持原始图像尺寸的纵横比。如果宽度和高度均为负值，则使用原始图像尺寸。
     * </p>
     */
    public static ImageIcon loadIcon(String iconName, int w, int h) {
        return new ImageIcon(loadIcon(iconName).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    /**
     * 从项目资源目录"icon"加载图片, 优先使用缓存
     */
    public static ImageIcon loadIconEnabledCache(String iconName) {
        URL resource = getIconUrl(iconName);
        String key = resource.getPath();
        ImageIcon cacheIcon = ICON_CACHE_MAP.get(key);
        if (cacheIcon != null) {
            return cacheIcon;
        }
        ImageIcon imageIcon = loadIcon(resource);
        ICON_CACHE_MAP.put(key, imageIcon);
        return imageIcon;
    }

    /**
     * 从项目资源目录"icon"加载图片, 优先使用缓存
     * <p>
     * 如果宽度或高度为负数，则替换一个值以保持原始图像尺寸的纵横比。如果宽度和高度均为负值，则使用原始图像尺寸。
     * </p>
     */
    public static ImageIcon loadIconEnabledCache(String iconName, int w, int h) {
        URL resource = getIconUrl(iconName);
        String key = resource.getPath();
        ImageIcon cacheIcon = ICON_CACHE_MAP.get(key);
        if (cacheIcon != null) {
            return new ImageIcon(cacheIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        }
        ImageIcon imageIcon = loadIcon(iconName);
        ICON_CACHE_MAP.put(key, imageIcon);
        return new ImageIcon(imageIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    private static URL getIconUrl(String iconName) {
        return SwingUtil.class.getResource("/icon/" + iconName);
    }

    public static InputStream loadStream(String iconName) throws IOException {
        URL iconUrl = getIconUrl(iconName);
        return iconUrl.openStream();
    }
}
