package com.evilsymphony;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

class BackgroundMusic {
    // Clip object to play the audio file. The audio data is stored in a Clip object as a series of samples
    private Clip clip;
    public boolean isPlaying = false;
    private boolean musicOptionIsYes = true;


    // Play the audio file
    public void play(String songFilePath) {
        // Use the class loader to get the resource stream for the audio file.
        InputStream audioStream = getClass().getClassLoader().getResourceAsStream(songFilePath);
        try {
            // Get an AudioInputStream from the audio resource stream. Represents a stream of audio data.
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioStream);
            // Get the audio format of the audio file
            AudioFormat format = ais.getFormat();
            // Create an Info object for the Clip class using the audio format
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            // Get a Clip object from the AudioSystem using the Info object. I believe this step is needed
            clip = (Clip) AudioSystem.getLine(info);
            // Open the Clip object with the audio input stream
            clip.open(ais);
            setVolume(12);
            // Start playing the audio
            clip.start();
            // To make the audio loop continuously, set the loop points.
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            // set isPlaying bool to true
            isPlaying = true;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            // Print stack trace if an error occurs
            e.printStackTrace();
        }
    }

    // Stop the cassette
    public void stop() {
        // Check if the Clip object exists
        if (clip != null) {
            // Stop playing the cassette
            clip.stop();
            // set isPlaying bool to false
            isPlaying = false;
        }
    }


    public void setVolume(int volume) {
        // the actual range is -80.0 to 6.0206 dB. this conversion allows for a range of 1-20 and is adjusted
        float dB = ((float) volume / 20 * 46) - 40;
        if (clip != null) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeControl != null) {
                volumeControl.setValue(dB);
            }
        }
    }

    // Prompt the user to set the volume
    public void promptVolume() {
        // Scanner to read the user input
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the audio level (1 - 20 ): ");
        // Call the setVolume method with the user input as the argument
        setVolume(input.nextInt());
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }


    public boolean MusicOptionIsYes() {
        return musicOptionIsYes;
    }

    public void setMusicOptionIsYes(boolean musicOptionIsYes) {
        this.musicOptionIsYes = musicOptionIsYes;
    }
}
