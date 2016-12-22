package com.ecs160.nittacraft;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class CSoundLibraryMixer {
    class SClipStatus{
        int DIdentification;
        int DIndex;
        int DOffset;
        float DVolume;
        float DRightBias;
    }

    class SToneStatus {
        int DIdentification;
        float DCurrentFrequency;
        float DCurrentStep;
        float DFrequencyDecay;
        float DVolume;
        float DVolumeDecay;
        float DRightBias;
        float DRightShift;
    }

    Context DContext;
    MediaPlayer DMediaPlayer;
    CSoundClip DSoundClips;
    // TODO: Remove public visibility once done testing
    public TreeMap< String, Integer > DMapping;
    public TreeMap< String, Integer > DMusicMapping;

    public CSoundLibraryMixer() {
        DMapping = new TreeMap<>();
        DMusicMapping = new TreeMap<>();
        DSoundClips = new CSoundClip();
        DMediaPlayer = new MediaPlayer();
        DMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * Loads the sound and music files.
     * @param source InputStream of the sound_clips.dat file.
     * @return boolean If file read correctly, or not.
     */
    public boolean LoadLibrary(CDataSource source, Context context) {
        DContext = context;
        BufferedReader SourceBuffer = new BufferedReader(new InputStreamReader(source.DInputStream));
        String TempString, TempString2;
        int TotalClips, TotalSongs;
        Resources R = context.getResources();

        // Reading number of clips
        try {
            TempString = SourceBuffer.readLine();
            TotalClips = Integer.parseInt(TempString);
        } catch(IOException e) {
            Log.e("CSoundLibraryMixer", "Failed to read clip count.\n");
            return false;
        }

        // Loading asset sounds
        for (int Index = 0; Index < TotalClips; Index++) {
            // Get clip name
            try {
                TempString = SourceBuffer.readLine();
                DMapping.put(TempString, Index);
            } catch(IOException e) {
                Log.e("CSoundLibraryMixer", "Failed to read clip name.\n");
                return false;
            }

            // Get clip path (Raw resource name)
            try {
                TempString = SourceBuffer.readLine();
            } catch(IOException e) {
                Log.e("CSoundLibraryMixer", "Failed to read clip path.\n");
                return false;
            }

            // Sets Sound IDs for SoundPool
            if (!DSoundClips.Load(TempString, context)) {
                Log.e("CSoundLibraryMixer", "Failed to load clip.\n");
                return false;
            }
        }

        // Reading number of songs
        try {
            TempString = SourceBuffer.readLine();
            TotalSongs = Integer.parseInt(TempString);
        } catch(IOException e) {
            Log.e("CSoundLibraryMixer", "Failed to read song count.\n");
            return false;
        }

        // Loading music
        for (int Index = 0; Index < TotalSongs; Index++) {
            // Get song name
            try {
                TempString = SourceBuffer.readLine();
            } catch(IOException e) {
                Log.e("CSoundLibraryMixer", "Failed to read song name.\n");
                return false;
            }

            // Get song path (Raw resource)
            try {
                TempString2 = SourceBuffer.readLine();
            } catch(IOException e) {
                Log.e("CSoundLibraryMixer", "Failed to read song path.\n");
                return false;
            }

            // Initialize MusicMapping with Strings to Resource ints
            int Song = R.getIdentifier(TempString2, "raw", context.getPackageName());
            if (Song == 0) {
                Log.e("CSoundLibraryMixer", "Failed to get song" + TempString2 + "\n");
                return false;
            }
            DMusicMapping.put(TempString, Song);
        }
        return true;
    }

    public int FindClip(String clipname) {
        Integer index = DMapping.get(clipname);
        if (index != null) {
            return index;
        }
        return -1;
    }

    /**
     * Passes parameters to CSoundClip, which plays the SoundPool clip.
     * TODO: Need to add the "identification number" from SClipStatus
     * @param index Which sound clip to play
     * @param volume Volume of the clip.
     * @param rightbias Which side of the audio to favor
     * @return SClipStatus identification number
     */
    public int PlayClip(int index, float volume, float rightbias) {
        SClipStatus TempClipStatus;

        if ((0 > index) || (DSoundClips.TotalClips() <= index)) {
            Log.e("CSoundLibraryMixer", "Invalid clip" + index + "!\n");
            return -1;
        }

        DSoundClips.PlayClip(index, volume, rightbias);
        return 1;
    }

    /**
     * Returns the song resource ID of the songname passed in
     * @param songname Name of song
     * @return Resource ID for the song
     */
    public int FindSong(String songname) {
        Integer SongID = DMusicMapping.get(songname);
        if (SongID != null) {
            return SongID;
        }
        return -1;
    }

    public void PlaySong(int index, float volume) {
        if (DMediaPlayer.isPlaying()) {
            StopSong();
        }

        Uri uri = Uri.parse("android.resource://" + DContext.getPackageName() + "/" + Integer.toString(index));
        Log.d("testing", uri.toString());
        try {
            DMediaPlayer.reset();
            DMediaPlayer.setDataSource(DContext, uri, null);
            DMediaPlayer.setLooping(true);
            DMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                   DMediaPlayer.start();
                }
            });
        } catch(IOException e) {
            Log.e("CSoundLibraryMixer", "Failed to play song.\n");
            return;
        }
        DMediaPlayer.prepareAsync();
    }

    public void StopSong() {
        if (DMediaPlayer.isPlaying())
            DMediaPlayer.stop();
    }
}
