import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundTest {
    public static void main(String[] args) {
        SoundTest soundTest = new SoundTest();
        // Play the sound with volume control
        System.out.println("Trying to play sound using direct file path with volume control...");
        soundTest.playExplosionSoundWithMaxVolume();
    }

    // Play sound using a direct file path and set volume to maximum
    private void playExplosionSoundWithMaxVolume() {
        try {
            // Directly access the .wav file using a relative or absolute path
            // Make sure to replace this path with the actual path of your explosion.wav file
            String filePath = "Resources/MenuMusic.wav"; // Example relative path, update accordingly
            File soundFile = new File(filePath);

            // Check if the file exists
            if (!soundFile.exists()) {
                System.err.println("Sound file not found at path: " + soundFile.getAbsolutePath());
                return;
            }

            // Use AudioSystem to get an audio input stream from the file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            // Obtain a clip to play the sound
            Clip clip = AudioSystem.getClip();
            // Open the clip with the audio stream
            clip.open(audioInputStream);

            // Get the volume control for the clip and set the volume to the maximum
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            System.out.println("Setting volume to maximum: " + volumeControl.getMaximum());
            volumeControl.setValue(volumeControl.getMaximum()); // Set to maximum volume

            // Print clip duration for debugging
            System.out.println("Clip duration (microseconds): " + clip.getMicrosecondLength());

            // Play the clip in a loop for longer playback (5 seconds)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            // Let the sound play for at least 5 seconds
            System.out.println("Clip started. Playing sound for 5 seconds...");
            Thread.sleep(10000); // Adjust this to hear the sound for a longer duration
            clip.stop();
            System.out.println("Clip stopped.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error playing explosion sound: " + e.getMessage());
        }
    }
}
