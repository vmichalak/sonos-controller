package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import com.vmichalak.sonoscontroller.exception.UPnPSonosControllerException;
import com.vmichalak.sonoscontroller.testcategory.UnmockedTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * To run this class, you have to be connected to a network with SONOS devices on (with at least one device not
 * connected to TV input).
 * @author Valentin Michalak
 */
@Category(UnmockedTest.class)
public class NonMockedSonosDeviceTest {
    private SonosDevice sonosDevice;

    @Before
    public void setUp() throws IOException {
        this.sonosDevice = SonosDiscovery.discoverOne();
        System.out.println(sonosDevice);
    }

    @Test
    public void testPlayPauseStop() throws IOException, SonosControllerException, InterruptedException {
        try {
            sonosDevice.stop();
            Thread.sleep(1000);
            assertEquals(PlayState.STOPPED, sonosDevice.getPlayState());
            sonosDevice.play();
            Thread.sleep(1000);
            assertEquals(PlayState.PLAYING, sonosDevice.getPlayState());
            sonosDevice.pause();
            Thread.sleep(1000);
            assertEquals(PlayState.PAUSED_PLAYBACK, sonosDevice.getPlayState());
        } catch (UPnPSonosControllerException e) {
            if(e.getErrorCode() == 701) { // Problem with playbar on TV input
                this.setUp();
                this.testPlayPauseStop();
            }
            else throw e;
        }
    }

    @Test
    public void testPlayMode() throws IOException, SonosControllerException, InterruptedException {
        try {
            PlayMode initialMode = sonosDevice.getPlayMode();
            sonosDevice.setPlayMode(PlayMode.NORMAL);
            Thread.sleep(100);
            assertEquals(PlayMode.NORMAL, sonosDevice.getPlayMode());
            sonosDevice.setPlayMode(PlayMode.REPEAT_ALL);
            Thread.sleep(100);
            assertEquals(PlayMode.REPEAT_ALL, sonosDevice.getPlayMode());
            sonosDevice.setPlayMode(PlayMode.REPEAT_ONE);
            Thread.sleep(100);
            assertEquals(PlayMode.REPEAT_ONE, sonosDevice.getPlayMode());
            sonosDevice.setPlayMode(PlayMode.SHUFFLE);
            Thread.sleep(100);
            assertEquals(PlayMode.SHUFFLE, sonosDevice.getPlayMode());
            sonosDevice.setPlayMode(PlayMode.SHUFFLE_NOREPEAT);
            Thread.sleep(100);
            assertEquals(PlayMode.SHUFFLE_NOREPEAT, sonosDevice.getPlayMode());
            sonosDevice.setPlayMode(initialMode);
        } catch (UPnPSonosControllerException e) {
            if(e.getErrorCode() == 712) { // Problem with playbar on TV input
                this.setUp();
                this.testPlayPauseStop();
            }
            else throw e;
        }
    }

    @Test
    public void testVolume() throws IOException, SonosControllerException, InterruptedException {
        int initialVolume = sonosDevice.getVolume();
        sonosDevice.setVolume(0);
        Thread.sleep(100);
        assertEquals(0, sonosDevice.getVolume());
        sonosDevice.setVolume(10);
        Thread.sleep(100);
        assertEquals(10, sonosDevice.getVolume());
        sonosDevice.setVolume(20);
        Thread.sleep(100);
        assertEquals(20, sonosDevice.getVolume());
        sonosDevice.setVolume(initialVolume);
    }

    @Test
    public void testMute() throws IOException, SonosControllerException, InterruptedException {
        boolean initialState = sonosDevice.isMuted();
        sonosDevice.setMute(true);
        Thread.sleep(100);
        assertEquals(true, sonosDevice.isMuted());
        sonosDevice.setMute(false);
        Thread.sleep(100);
        assertEquals(false, sonosDevice.isMuted());
        sonosDevice.setMute(initialState);
    }

    @Test
    public void testBass() throws IOException, SonosControllerException, InterruptedException {
        int initialValue = sonosDevice.getBass();
        Thread.sleep(100);
        sonosDevice.setBass(3);
        assertEquals(3, sonosDevice.getBass());
        Thread.sleep(100);
        sonosDevice.setBass(initialValue);
    }

    @Test
    public void testTreble() throws IOException, SonosControllerException, InterruptedException {
        int initialValue = sonosDevice.getTreble();
        sonosDevice.setTreble(3);
        Thread.sleep(100);
        assertEquals(3, sonosDevice.getTreble());
        sonosDevice.setTreble(initialValue);
    }

    @Test
    public void testLoudness() throws IOException, SonosControllerException, InterruptedException {
        boolean initialState = sonosDevice.isLoudnessActivated();
        sonosDevice.setLoudness(false);
        Thread.sleep(100);
        assertEquals(false, sonosDevice.isLoudnessActivated());
        sonosDevice.setLoudness(true);
        Thread.sleep(100);
        assertEquals(true, sonosDevice.isLoudnessActivated());
        sonosDevice.setLoudness(initialState);
    }

    @Test
    public void testZoneName() throws IOException, SonosControllerException, InterruptedException {
        String initialName = sonosDevice.getZoneName();
        sonosDevice.setZoneName("CURRENTLY IN TEST");
        Thread.sleep(100);
        assertEquals("CURRENTLY IN TEST", sonosDevice.getZoneName());
        sonosDevice.setZoneName(initialName);
    }

    @Test
    public void testLedDifferentState() throws IOException, SonosControllerException, InterruptedException {
        boolean initialState = sonosDevice.getLedState();
        sonosDevice.setLedState(true);
        assertEquals(true, sonosDevice.getLedState());
        Thread.sleep(100);
        sonosDevice.switchLedState();
        assertEquals(false, sonosDevice.getLedState());
        Thread.sleep(100);
        sonosDevice.setLedState(false);
        assertEquals(false, sonosDevice.getLedState());
        Thread.sleep(100);
        sonosDevice.setLedState(true);
        assertEquals(true, sonosDevice.getLedState());
        sonosDevice.setLedState(initialState);
    }
}
