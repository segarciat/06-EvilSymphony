package com.evilsymphony;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

class BackgroundMusic {
    // Clip object to play the audio file. The audio data is stored in a Clip object as a series of samples
    private Clip clip;

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
            // Start playing the audio
            clip.start();
            // To make the audio loop continuously, set the loop points.
            clip.loop(Clip.LOOP_CONTINUOUSLY);
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
        }
    }

    // Set the volume of the cassette
    public void setVolume(float volume) {
        if (clip != null) {
            // Get the volume control for the Clip object
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeControl != null) {
                // Set the volume to the desired level
                volumeControl.setValue(volume);
            }
        }
    }

    // Prompt the user to set the volume
    public void promptVolume() {
        // Scanner to read the user input
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the audio level (0.0 to 1.0): ");
        // Call the setVolume method with the user input as the argument
        setVolume(input.nextFloat());
    }

    public void backgroundSongs(String location) {

        switch (location) {
            case "MAIN HALL":
                stop();
                play("mainHallSong.wav");
                break;
            case "STAGE":
                stop();
                play("stageSong.wav");
                break;
            case "BACKSTAGE":
                stop();
                play("backstageSong.wav");
                break;
            case "BAR":
                stop();
                play("barSong.wav");
                break;
            case "BAND DRESSING ROOM":
                stop();
                play("bandDressingRoomSong.wav");
                break;

            default:

        }

    }

}