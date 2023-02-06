package com.evilsymphony.util;

import com.evilsymphony.util.TextParser;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class BackgroundMusic {
    // Clip object to play the audio file. The audio data is stored in a Clip object as a series of samples
    private Clip clip;
    public boolean isPlaying = false;
    private boolean musicOptionIsYes = true;
    private final String[] soundFiles = {"sound1.wav", "sound2.wav", "sound3.wav"};
    private int currentVolume = 12;
    private TextParser parser;


    public BackgroundMusic(TextParser parser) {
        this.parser = parser;
    }

    // Play the audio file
    public void play(String songFilePath) {
        songFilePath = "music/" + songFilePath;
        // Use the class loader to get the resource stream for the audio file.
        BufferedInputStream audioStream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(songFilePath));
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
            setVolume(currentVolume);
            // Start playing the audio
            clip.start();
            // To make the audio loop continuously, set the loop points.
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            // set isPlaying bool to true
            isPlaying = true;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            // Print stack trace if an error occurs
            throw new RuntimeException(e);
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
        currentVolume = Integer.parseInt(parser.prompt("Enter the audio level (1 - 20 ): ","^(1[0-9]|[1-9]|20)$","Please enter a number 1-20"));
        setVolume(currentVolume);

    }

    public boolean startSoundGame() {
        System.out.println("Listen closely to the next three sounds to determine which is the lowest");
        for (int i = 0; i < 3; i++) {
            // Play each sound file
            play(soundFiles[i]);

            // 3 second pause
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Stop the current sound
            stop();
        }

        System.out.println("Which sound had the lowest note?");
        System.out.println("Enter 1, 2, or 3");

        // Playe's choice
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        System.out.println("You chose sound " + choice + ".");

        // randomize this somehow
        return choice == 2;
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
