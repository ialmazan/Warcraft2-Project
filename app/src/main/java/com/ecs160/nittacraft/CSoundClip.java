package com.ecs160.nittacraft;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;

public class CSoundClip {
    int TotalClips;
    SoundPool DSoundPool;
    ArrayList< Integer > DSoundIds;

    public CSoundClip() {
        DSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        DSoundIds = new ArrayList<>();
    }

    int TotalClips() {
        return DSoundIds.size();
    }

    /**
     * Loads the wav files for the SoundPool, and saves the ID.
     * @param soundclip Name of the resource to be saved.
     * @param context Context to load resources.
     * @return boolean If file loaded correctly, or not.
     */
    boolean Load(String soundclip, Context context) {
        Resources R = context.getResources();
        int SoundClip = R.getIdentifier(soundclip, "raw", context.getPackageName());

        if (SoundClip != 0) {
            int SoundId = DSoundPool.load(context, SoundClip, 1);
            DSoundIds.add(SoundId);
            return true;
        }
        return false;
    }

    void PlayClip(int index, float volume, float rightbias) {
        DSoundPool.play(DSoundIds.get(index), volume, volume, 0, 0, 1.0f);
    }
    /*
    protected int DChannels;
    protected int DTotalFrames;
    protected int DSampleRate;
    protected float DData;

    public CSoundClip() {
        DData = null;
        DChannels = 0;
        DTotalFrames = 0;
        DSampleRate = 0;
    }

    public CSoundClip(const CSoundClip &clip) {
        DChannels = clip.DChannels;
        DTotalFrames = clip.DTotalFrames;
        DSampleRate = clip.DSampleRate;
        DData = NULL;
        if (DChannels * DTotalFrames) {
            DData = new float[DChannels * DTotalFrames];
            memcpy(DData, clip.DData, sizeof(float) * DTotalFrames * DChannels);
        }
    }

    CSoundClip &operator=(const CSoundClip &clip);

    int Channels() {
        return DChannels;
    }

    int TotalFrames() {
        return DTotalFrames;
    }

    int SampleRate() {
        return DSampleRate;
    }

    boolean Load(CDataSource source) {
        /* FIXME: Needed?
        CSFVirtualIODataSource VIODataSource(source);
        SF_VIRTUAL_IO SFVirtualIO = VIODataSource.SFVirtualIO();
        SNDFILE *SoundFilePtr;
        SF_INFO SoundFileInfo;

        //SoundFilePtr = sf_open(filename.c_str(), SFM_READ, &SoundFileInfo);
        SoundFilePtr = sf_open_virtual(&SFVirtualIO, SFM_READ, &SoundFileInfo, (void *)&VIODataSource);
        if (NULL == SoundFilePtr) {
            return false;
        }
        if (1 == SoundFileInfo.channels) {
            DChannels = 2;
            DTotalFrames = SoundFileInfo.frames;
            DSampleRate = SoundFileInfo.samplerate;
            DData = new float[SoundFileInfo.frames * 2];
            for (int Frame = 0; Frame < DTotalFrames; Frame++) {
                sf_readf_float(SoundFilePtr, DData + Frame * 2, 1);
                DData[Frame * 2 + 1] = DData[Frame * 2];
            }
        }
        else if (2 == SoundFileInfo.channels) {
            DChannels = 2;
            DTotalFrames = SoundFileInfo.frames;
            DSampleRate = SoundFileInfo.samplerate;
            DData = new float[SoundFileInfo.frames * SoundFileInfo.channels];
            sf_readf_float(SoundFilePtr, DData, DTotalFrames);
        }
        else {
            sf_close(SoundFilePtr);
            return false;
        }

        sf_close(SoundFilePtr);

        return true;
    }

    void CopyStereoClip(float *data, int offset, int frames) {
        if (offset + frames > DTotalFrames) {
            int FramesToCopy = DTotalFrames - offset;

            if (0 > FramesToCopy) {
                FramesToCopy = 0;
            }
            if (FramesToCopy) {
                memcpy(data, DData + (offset * 2), sizeof(float) * FramesToCopy * 2);
            }
            memset(data, 0, sizeof(float) * (frames - FramesToCopy) * 2);
        }
        else {
            memcpy(data, DData + (offset * 2), sizeof(float) * frames * 2);
        }
    }

    void MixStereoClip(float *data, int offset, int frames, float volume = 1.0, float rightbias = 0.0) {
        float *DataPointer = DData + (offset * 2);
        int FramesToMix = frames;

        if (offset + frames > DTotalFrames) {
            FramesToMix = DTotalFrames - offset;
            if (0 > FramesToMix) {
                FramesToMix = 0;
            }
        }
        for (int Frame = 0; Frame < FramesToMix; Frame++) {
            *data++ += volume * (1.0 - rightbias) * *DataPointer++;
            *data++ += volume * (1.0 + rightbias) * *DataPointer++;
        }
    }
*/
}
