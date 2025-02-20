package com.am.design.development.utilities.utils;

import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVOutputFormat;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;

import java.io.File;
import java.io.IOException;

import static org.bytedeco.ffmpeg.global.avcodec.av_packet_alloc;
import static org.bytedeco.ffmpeg.global.avcodec.av_packet_unref;
import static org.bytedeco.ffmpeg.global.avformat.*;
import static org.bytedeco.ffmpeg.global.avutil.av_dict_set;

public class VideoConverter {

    public static void convertToMp4(File input, File output) throws IOException {

        // Crea il contesto di input e output
        AVFormatContext inputContext = new AVFormatContext(null);
        AVFormatContext outputContext = new AVFormatContext(null);

        // Apri il file di input
        if (avformat_open_input(inputContext, input.getCanonicalPath(), null, null) != 0) {
            throw new RuntimeException("Errore nell'apertura del file di input.");
        }

        // Crea il contesto di output per MP4
        if (avformat_alloc_output_context2(outputContext, null, "mp4", output.getCanonicalPath()) < 0) {
            throw new RuntimeException("Errore nell'apertura del file di output.");
        }

        // Aggiungi i flussi al contesto di output
        AVOutputFormat outputFormat = outputContext.oformat();
        outputFormat.video_codec(avcodec.AV_CODEC_ID_H264);
        outputFormat.audio_codec(avcodec.AV_CODEC_ID_AAC);

        // Scrivi l'header del file di output
        AVDictionary options = new AVDictionary(null);
        av_dict_set(options, "movflags", "faststart", 0);
        if (avformat_write_header(outputContext, options) < 0) {
            throw new RuntimeException("Errore nella scrittura dell'header del file di output.");
        }

        // Leggi pacchetti dal file di input e scrivili nel file di output
        AVPacket packet = av_packet_alloc();
        while (avformat.av_read_frame(inputContext, packet) >= 0) {
            avformat.av_write_frame(outputContext, packet);
            av_packet_unref(packet);
        }

        // Chiudi i contesti
        avformat_close_input(inputContext);
        avformat_free_context(outputContext);

    }
}
