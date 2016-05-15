 package com.hs.captcha.component.image;
 
 import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hs.captcha.common.xml.FontConfig;
import com.hs.captcha.model.ImageCaptcha;
import com.jhlabs.image.RippleFilter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

 
 public class ImageCaptchaUtil
 {
   public static final Log logger = LogFactory.getLog(ImageCaptchaUtil.class);
   private static final int bgColor = 16777215;
   private static final int height = 50;
   private static final int charHeight = 40;
   private static final int charWidth = 31;
   private static final int[] fgColorRange = { 0, 200 };
   private static RippleFilter rippleFilter = new RippleFilter();
   private static final int bgCharsCountForEachChar = 15;
   
   public static ImageCaptcha getGifImage(String code, FontConfig fontConfig, boolean appendInterference)
   {
     return genGifImage(code, fontConfig, null, appendInterference);
   }
   
   public static ImageCaptcha getGifImage(String code, FontConfig fontConfig, int imageWidth, boolean appendInterference)
   {
     return genGifImage(code, fontConfig, Integer.valueOf(imageWidth), appendInterference);
   }
   
   public static ImageCaptcha getJpegImage(String code, FontConfig fontConfig, boolean appendInterference)
     throws IOException
   {
     return genJpegImage(code, fontConfig, null, appendInterference);
   }
   
   public static ImageCaptcha getJpegImage(String code, FontConfig fontConfig, int imageWidth, boolean appendInterference)
     throws IOException
   {
     return genJpegImage(code, fontConfig, Integer.valueOf(imageWidth), appendInterference);
   }
   
   private static ImageCaptcha genGifImage(String code, FontConfig fontConfig, Integer imageWidth, boolean appendInterference)
   {
	   ByteArrayOutputStream stream = new ByteArrayOutputStream();
     
     AnimatedGifEncoder agf = new AnimatedGifEncoder();
     agf.start(stream);
     agf.setQuality(10);
     
     agf.setDelay(200);
     agf.setRepeat(0);
     
     char[] codeChars = code.toCharArray();
     
     Color[] fontColors = genFontColors(codeChars.length);
     
     BufferedImage[] codeImages = getCharsImage(codeChars, fontColors, fontConfig, charHeight);
     
 
     int sumBlankSpace = 0;
     int imageWidthOfRemoveBlank = 0;
     for (int i = 0; i < codeImages.length; i++)
     {
       imageWidthOfRemoveBlank += codeImages[i].getWidth();
       if (i <= 0) {
         sumBlankSpace += leftBlankNum(codeImages[i], 0);
       } else {
         sumBlankSpace += calculateDistanceBetweenChar(codeImages[(i - 1)], codeImages[i], 0);
       }
     }
     imageWidthOfRemoveBlank -= sumBlankSpace;
     
 
     BackgroudChar[] backgroudChars = null;
     if (appendInterference) {
       backgroudChars = new BackgroudChar[bgCharsCountForEachChar * codeChars.length];
     }
     BufferedImage frameImage = null;
     int[] codeImageXPosOfFirstFrame = new int[codeChars.length];
     for (int i = 0; i < 4; i++)
     {
       if (null == imageWidth) {
         imageWidth = Integer.valueOf(charWidth * codeChars.length);
       }
       frameImage = getFrameImage(imageWidth.intValue(), height, imageWidthOfRemoveBlank, codeImages, codeImageXPosOfFirstFrame, fontConfig, backgroudChars, i, appendInterference);
       agf.addFrame(frameImage);
       frameImage.flush();
     }
     agf.finish();
     
     ImageCaptcha ca = new ImageCaptcha();
     ca.setCode(code);
     ca.setContentType(ContentTypeEnum.GIF);
     ca.setChallenge(stream);
     return ca;
   }
   
   @SuppressWarnings("restriction")
private static ImageCaptcha genJpegImage(String code, FontConfig fontConfig, Integer imageWidth, boolean appendInterference)
     throws IOException
   {
     char[] codeChars = code.toCharArray();
     
     Color[] fontColors = genFontColors(codeChars.length);
     
     BufferedImage[] codeImages = getCharsImage(codeChars, fontColors, fontConfig, charHeight);
     
     BufferedImage bi = null;
     if (null != imageWidth) {
       bi = appendImages(codeImages, fontConfig.getSpacingSize(), imageWidth.intValue());
     } else {
       bi = appendImages(codeImages, fontConfig.getSpacingSize());
     }
     if (appendInterference)
     {
       AlphaComposite backComposite = AlphaComposite.getInstance(3, 0.2F);
       Graphics2D g2d = bi.createGraphics();
       g2d.setComposite(backComposite);
       char[] dictionaryChars = fontConfig.getDict().toCharArray();
       g2d.setFont(new Font("Arial", 3, 16));
       for (int i = 0; i < bgCharsCountForEachChar * codeChars.length; i++)
       {
         g2d.setColor(fontColors[new Random().nextInt(fontColors.length)]);
         g2d.drawString(Character.toString(dictionaryChars[new Random().nextInt(dictionaryChars.length)]), (int)(Math.random() * bi.getWidth()), (int)(Math.random() * bi.getHeight()));
       }
       g2d.dispose();
       
 
       appendInterference(bi, fontColors, codeChars.length);
     }
     ImageCaptcha ca = new ImageCaptcha();
     ca.setCode(code);
     ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
     JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
     jpegEncoder.encode(bi);
     ca.setChallenge(jpegOutputStream);
     ca.setContentType(ContentTypeEnum.JPEG);
     return ca;
   }
   
   private static Color[] genFontColors(int count)
   {
     Color[] fontColors = new Color[count];
     for (int i = 0; i < count; i++) {
       fontColors[i] = getColor(fgColorRange);
     }
     return fontColors;
   }
   
   private static BufferedImage[] getCharsImage(char[] codeChars, Color[] fontColors, FontConfig fontConfig, int height)
   {
     BufferedImage[] images = new BufferedImage[codeChars.length];
     for (int i = 0; i < codeChars.length; i++)
     {
       images[i] = new BufferedImage(height, height, 1);
       Graphics2D g2d = images[i].createGraphics();
       images[i] = g2d.getDeviceConfiguration().createCompatibleImage(height, height, 3);
       g2d.dispose();
       g2d = images[i].createGraphics();
       int r = (int)(Math.random() * fontConfig.getFontList().size());
       g2d.setFont((Font)fontConfig.getFontList().get(r));
       g2d.setFont(g2d.getFont().deriveFont(1));
       g2d.setColor(fontColors[i]);
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       
       int p_x = (height - g2d.getFontMetrics().charWidth(codeChars[i])) / 2;
       int rand = (int)(Math.random() / 2.0D * (height - g2d.getFontMetrics().getHeight()));
       int p_y = fontConfig.getFontSizeMax() + rand;
       g2d.drawString(Character.toString(codeChars[i]), p_x, p_y);
       g2d.dispose();
       images[i] = rotateImage(images[i], new int[] { -fontConfig.getRotateRange(), fontConfig.getRotateRange() });
       
 
       float rSin = getRandomInRange(new float[] { fontConfig.getSinMin(), fontConfig.getSinMax() });
       if (new Random().nextInt(10) % 2 != 0) {
         rSin = -rSin;
       }
       float rWave = getRandomInRange(new float[] { fontConfig.getWaveMin(), fontConfig.getWaveMax() });
       images[i] = sin(images[i], rSin, rWave);
     }
     return images;
   }
   
   private static BufferedImage appendImages(BufferedImage[] images, int spacingSize)
   {
     return appendImages(images, spacingSize, charWidth * images.length);
   }
   
   private static BufferedImage appendImages(BufferedImage[] images, int spacingSize, int imageWidth)
   {
     BufferedImage bgImage = new BufferedImage(imageWidth, height, 4);
     Graphics2D g2d = bgImage.createGraphics();
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     g2d.setColor(new Color(bgColor));
     g2d.fillRect(0, 0, imageWidth * images.length, height);
     g2d.setColor(Color.white);
     g2d.drawRect(0, 0, imageWidth * images.length, height);
     int index = 0;
     int drawX = 0;
     if ((images != null) && (images.length != 0))
     {
       int distance = 0;
       int trueImageWidth = 0;
       for (int i = 0; i < images.length; i++)
       {
         trueImageWidth += images[i].getWidth();
         if (i <= 0) {
           distance += leftBlankNum(images[i], spacingSize);
         } else {
           distance += calculateDistanceBetweenChar(images[(i - 1)], images[i], spacingSize);
         }
       }
       trueImageWidth -= distance;
       
       drawX = imageWidth > trueImageWidth ? new Random().nextInt(imageWidth - trueImageWidth) : 0;
       int drowY = new Random().nextInt(height - images[index].getHeight());
       g2d.drawImage(images[index], drawX, drowY, images[index].getWidth(), images[index].getHeight(), null);
       drawX += images[index].getWidth();
       index++;
     }
     while (index < images.length)
     {
       int distance = calculateDistanceBetweenChar(images[(index - 1)], images[index], spacingSize);
       int drowY = new Random().nextInt(height - images[index].getHeight());
       g2d.drawImage(images[index], drawX - distance, drowY, images[index].getWidth(), images[index].getHeight(), null);
       drawX += images[index].getWidth() - distance;
       index++;
     }
     g2d.dispose();
     return bgImage;
   }
   
   private static void appendInterference(BufferedImage image, Color[] fgColors, int codeCount)
   {
     Graphics2D g2d = image.createGraphics();
     Random random = new Random();
 
 
     int charWidth = image.getWidth() / codeCount;
     Stroke drawingStroke = new BasicStroke(3.0F);
     
     g2d.setStroke(drawingStroke);
     
 
     double lastY = 0.0D;
     for (int i = 0; i < codeCount; i++)
     {
       if (lastY == 0.0D) {
         lastY = random.nextInt(10) + 20;
       }
       double y1 = lastY;
       double y2 = random.nextInt(10) + 20;
       double y3 = random.nextInt(10) + 20;
       QuadCurve2D curve = new QuadCurve2D.Double(2 * i * (charWidth / 2), y1, (2 * i + 1) * (charWidth / 2), y2, (2 * i + 2) * (charWidth / 2), y3);
       
 
 
       g2d.setPaint(fgColors[i]);
       g2d.draw(curve);
       lastY = y3;
     }
     g2d.dispose();
   }
   
   public static BufferedImage sin(BufferedImage src, float amplitude, float wavelength)
   {
     rippleFilter.setWaveType(0);
     rippleFilter.setXAmplitude(amplitude);
     rippleFilter.setXWavelength(wavelength);
     rippleFilter.setEdgeAction(1);
     return rippleFilter.filter(src, null);
   }
   
   private static int leftBlankNum(BufferedImage img, int spacingSize)
   {
     int[][] left = calculateBlankNum(img, spacingSize);
     int[] tempArray = new int[img.getHeight()];
     for (int i = 0; i < left.length; i++) {
       tempArray[i] = left[i][0];
     }
     return min(tempArray);
   }
   
   private static int min(int[] array)
   {
     int result = 2147483647;
     for (int item : array) {
       if (item < result) {
         result = item;
       }
     }
     return result;
   }
   
   private static int[][] calculateBlankNum(BufferedImage image, int spacingSize)
   {
     int width = image.getWidth();
     int height = image.getHeight();
     int[][] result = new int[height][2];
     for (int i = 0; i < result.length; i++)
     {
       result[i][0] = 0;
       result[i][1] = width;
     }
     int[] colorArray = new int[4];
     for (int i = 0; i < height; i++) {
       for (int j = 0; j < width; j++)
       {
         colorArray = image.getRaster().getPixel(j, i, colorArray);
         if (!checkArray(colorArray, new int[] { 0, 0, 0, 0 }))
         {
           if (result[i][0] == 0) {
             result[i][0] = j;
           }
           result[i][1] = (width - j + spacingSize);
         }
       }
     }
     return result;
   }
   
   private static int calculateDistanceBetweenChar(BufferedImage leftImage, BufferedImage rightImage, int spacingSize)
   {
     int[][] left = calculateBlankNum(leftImage, spacingSize);
     int[][] right = calculateBlankNum(rightImage, spacingSize);
     int[] tempArray = new int[leftImage.getHeight()];
     for (int i = 0; i < left.length; i++) {
       if (right[i][0] == 0) {
         tempArray[i] = (left[i][1] + leftImage.getWidth());
       } else {
         tempArray[i] = (left[i][1] + right[i][0]);
       }
     }
     return min(tempArray);
   }
   
   private static boolean checkArray(int[] arrayA, int[] arrayB)
   {
     if ((arrayA == null) || (arrayB == null)) {
       return false;
     }
     if (arrayA.length != arrayB.length) {
       return false;
     }
     for (int i = 0; i < arrayA.length; i++) {
       if (arrayA[i] != arrayB[i]) {
         return false;
       }
     }
     return true;
   }
   
   private static float getRandomInRange(float[] range)
   {
     if ((range == null) || (range.length != 2)) {
       throw new RuntimeException("范围参数不合法，必须包含两个元素");
     }
     return (float)(Math.random() * (range[1] - range[0]) + range[0]);
   }
   
   private static BufferedImage getFrameImage(int imageWidth, int imageHeight, int imageWidthOfRemoveBlank, BufferedImage[] codeImages, int[] codeImageXPosOfFirstFrame, FontConfig fontConfig, BackgroudChar[] backgroudChars, int currentFrameIndex, boolean appendInterference)
   {
     BufferedImage image = new BufferedImage(imageWidth, imageHeight, 1);
     
 
     Graphics2D g2d = image.createGraphics();
     
     g2d.setColor(new Color(bgColor));
     g2d.fillRect(0, 0, imageWidth, imageHeight);
     
 
     AlphaComposite fontComposite = null;
     for (int i = 0; i < codeImages.length; i++)
     {
       fontComposite = AlphaComposite.getInstance(3, getAlpha(currentFrameIndex, i));
       g2d.setComposite(fontComposite);
       if (currentFrameIndex <= 0) {
         if (i < 1)
         {
           codeImageXPosOfFirstFrame[i] = (imageWidth > imageWidthOfRemoveBlank ? new Random().nextInt(imageWidth - imageWidthOfRemoveBlank) : 0);
         }
         else
         {
           int distance = calculateDistanceBetweenChar(codeImages[(i - 1)], codeImages[i], 0);
           codeImageXPosOfFirstFrame[i] = (codeImageXPosOfFirstFrame[(i - 1)] + codeImages[i].getWidth() - distance);
         }
       }
       g2d.drawImage(codeImages[i], codeImageXPosOfFirstFrame[i], new Random().nextInt(5), codeImages[i].getWidth(), codeImages[i].getHeight(), null);
     }
     if (appendInterference)
     {
       AlphaComposite backComposite = AlphaComposite.getInstance(3, 0.2F);
       g2d.setComposite(backComposite);
       char[] dictionaryChars = fontConfig.getDict().toCharArray();
       g2d.setFont(new Font("Arial", 3, 16));
       for (int i = 0; i < bgCharsCountForEachChar * codeImages.length; i++)
       {
         if (currentFrameIndex <= 0) {
           backgroudChars[i] = new BackgroudChar((int)(Math.random() * imageWidth), (int)(Math.random() * imageHeight), dictionaryChars[new Random().nextInt(dictionaryChars.length)], getColor(fgColorRange));
         }
         g2d.setColor(backgroudChars[i].getCurColor());
         g2d.drawString(Character.toString(backgroudChars[i].getCurChar()), backgroudChars[i].getX(), backgroudChars[i].getY());
       }
     }
     g2d.dispose();
     return image;
   }
   
   private static Color getColor(int[] colorRange)
   {
     int r = getRandomInRange(colorRange);
     int g = getRandomInRange(colorRange);
     int b = getRandomInRange(colorRange);
     return new Color(r, g, b);
   }
   
   private static int getRandomInRange(int[] range)
   {
     if ((range == null) || (range.length != 2)) {
       throw new RuntimeException("范围参数不合法，必须包含两个元素");
     }
     return new Random().nextInt(range[1]);
   }
   
   private static float getAlpha(int i, int j)
   {
     float newValue = new Random().nextFloat();
     if (newValue < 0.5F) {
       return 2.0F * newValue;
     }
     return 1.0F;
   }
   
   private static BufferedImage rotateImage(BufferedImage bufferedimage, int[] rotateRange)
   {
     int w = bufferedimage.getWidth();
     int h = bufferedimage.getHeight();
     int type = bufferedimage.getColorModel().getTransparency();
     BufferedImage img = new BufferedImage(w, h, type);
     Graphics2D graphics2d = img.createGraphics();
     graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
     
 
     int angdeg = getRandomInRange(rotateRange);
     if (angdeg <= 5) {
       angdeg += 5;
     }
     double theta = Math.toRadians(angdeg);
     if ((int)(Math.random() * 1000.0D) % 2 != 0) {
       theta = -theta;
     }
     graphics2d.rotate(theta, w / 2, h / 2);
     graphics2d.drawImage(bufferedimage, 0, 0, null);
     graphics2d.dispose();
     return img;
   }
   
   public static class BackgroudChar
   {
     private int X;
     private int Y;
     private char curChar;
     private Color curColor;
     
     public BackgroudChar(int x, int y, char c, Color color)
     {
       this.X = x;
       this.Y = y;
       this.curChar = c;
       this.curColor = color;
     }
     
     public void setX(int x)
     {
       this.X = x;
     }
     
     public void setY(int y)
     {
       this.Y = y;
     }
     
     public void setCurChar(char c)
     {
       this.curChar = c;
     }
     
     public void setCurColor(Color c)
     {
       this.curColor = c;
     }
     
     public int getX()
     {
       return this.X;
     }
     
     public int getY()
     {
       return this.Y;
     }
     
     public char getCurChar()
     {
       return this.curChar;
     }
     
     public Color getCurColor()
     {
       return this.curColor;
     }
   }
 }