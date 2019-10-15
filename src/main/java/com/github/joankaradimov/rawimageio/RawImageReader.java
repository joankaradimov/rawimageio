package com.github.joankaradimov.rawimageio;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

public class RawImageReader extends ImageReader {
    private static final ColorSpace RGB_COLOR_SPACE = ColorSpace.getInstance(ColorSpace.CS_sRGB);

    private RawImage rawImage = null;

    protected RawImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    public RawImage getRawImage() throws IOException {
        if (rawImage == null) {
            if (input instanceof ByteBuffer) {
                rawImage = new RawImage((ByteBuffer) input);
            } else if (input instanceof File) {
                rawImage = new RawImage(((File) input).getAbsolutePath());
            } else if (input instanceof String) {
                rawImage = new RawImage((String) input);
            } else if (input instanceof FileChannel) {
                FileChannel fileChannel = (FileChannel) input;
                long longSize = fileChannel.size();
                if (longSize > Integer.MAX_VALUE) {
                    throw new IOException("Input file channel is too long");
                }
                int size = (int) longSize;
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size);
                fileChannel.read(byteBuffer);
                byteBuffer.rewind();
                rawImage = new RawImage(byteBuffer, size);
            }
        }
        return rawImage;
    }

    @Override
    public int getNumImages(boolean b) throws IOException {
        return getRawImage().getImageParameters().getRawCount();
    }

    @Override
    public int getWidth(int i) throws IOException {
        return getRawImage().getIWidth();
    }

    @Override
    public int getHeight(int i) throws IOException {
        return getRawImage().getIHeight();
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int i) throws IOException {
        return null;
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    @Override
    public IIOMetadata getImageMetadata(int i) throws IOException {
        return null;
    }

    @Override
    public BufferedImage read(int i, ImageReadParam imageReadParam) throws IOException {
        RawImage rawImage = getRawImage();
        rawImage.unpack();
        rawImage.dcrawProcess();

        var image = rawImage.makeMemoryImage();
        var bytes = image.getDataInputStream().readAllBytes();
        var raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, image.getWidth(), image.getHeight(), image.getColors(), null);

        raster.setDataElements(0, 0, image.getWidth(), image.getHeight(), bytes);
        ComponentColorModel colorModel = new ComponentColorModel(RGB_COLOR_SPACE, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(colorModel, raster, false, null);
    }
}
