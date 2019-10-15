package com.github.joankaradimov.rawimageio;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Locale;

public class RawImageReaderSpi extends ImageReaderSpi {
    public RawImageReaderSpi() {
        // TODO: set vendorName
        // TODO: set version
        this.names = new String[] {
            "ARW", // Sony
            "3FR", // Hasselblad
            "CR2", // Canon
            "CRW", // Canon
            "DCR", // Kodak
            "DNG", // Leica, Nikon, Nokia, OnePlus, Pentax, Ricoh, Samsung
            "ERF", // Epson
            "KDC", // Kodak
            "MDC", // Minolta
            "MEF", // Mamiya
            "MOS", // Aptus
            "MRW", // Minolta
            "NEF", // Nikon
            "NRW", // Nikon
            "ORF", // Olympus
            "PEF", // Pentax
            "PPM", // Hasselblad, Aptus
            "RAF", // Fuji
            "RAW", // Kodak, Leica, Panasonic, Pentax
            "RW2", // Panasonic
            "SRF", // Sony
            "SRW", // Samsung
            "X3F", // Polaroid, Sigma
        };
        this.suffixes = new String[] {
            "ARW", // Sony
            "3FR", // Hasselblad
            "CR2", // Canon
            "CRW", // Canon
            "DCR", // Kodak
            "DNG", // Leica, Nikon, Nokia, OnePlus, Pentax, Ricoh, Samsung
            "ERF", // Epson
            "KDC", // Kodak
            "MDC", // Minolta
            "MEF", // Mamiya
            "MOS", // Aptus
            "MRW", // Minolta
            "NEF", // Nikon
            "NRW", // Nikon
            "ORF", // Olympus
            "orf", // Olympus
            "PEF", // Pentax
            "PPM", // Hasselblad
            "ppm", // Aptus,
            "RAF", // Fuji
            "RAW", // Kodak, Leica, Panasonic, Pentax
            "RW2", // Panasonic
            "rw2", // Panasonic
            "SRF", // Sony
            "SRW", // Samsung
            "X3F", // Polaroid, Sigma
        };
        this.MIMETypes = new String [] {
            "image/x-adobe-dng",
            "image/x-raw-aptus",
            "image/x-raw-canon",
            "image/x-raw-epson",
            "image/x-raw-fuji",
            "image/x-raw-hasselblad",
            "image/x-raw-kodak",
            "image/x-raw-mamiya",
            "image/x-raw-minolta",
            "image/x-raw-nikon",
            "image/x-raw-olympus",
            "image/x-raw-panasonic",
            "image/x-raw-pentax",
            "image/x-raw-samsung",
            "image/x-raw-sigma",
            "image/x-raw-sony",
        };
        pluginClassName = RawImageReader.class.getName();
        // TODO: set supportsStandardStreamMetadataFormat
        // TODO: set nativeStreamMetadataFormatName
        // TODO: set nativeStreamMetadataFormatClassName
        // TODO: set extraStreamMetadataFormatNames
        // TODO: set extraStreamMetadataFormatClassNames
        // TODO: set supportsStandardImageMetadataFormat
        // TODO: set nativeImageMetadataFormatName
        // TODO: set nativeImageMetadataFormatClassName
        // TODO: set extraImageMetadataFormatNames
        // TODO: set extraImageMetadataFormatClassNames
        this.inputTypes = new Class[] {
            ByteBuffer.class,
            File.class,
            String.class,
            FileChannel.class,
        };
    }

    @Override
    public boolean canDecodeInput(Object o) throws IOException {
        if (o instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer) o;
            return byteBuffer.isDirect();
        }
        // TODO: use inputTypes
        return o instanceof File || o instanceof String || o instanceof FileChannel;
    }

    @Override
    public ImageReader createReaderInstance(Object o) {
        var reader = new RawImageReader(this);
        reader.setInput(o);
        return reader;
    }

    @Override
    public String getDescription(Locale locale) {
        return "RAW digital photo camera image reader";
    }
}
