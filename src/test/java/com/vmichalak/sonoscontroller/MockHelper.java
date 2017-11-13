package com.vmichalak.sonoscontroller;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Matchers.anyString;

public class MockHelper {
    public static CommandBuilder mockCommandBuilder(String output) throws Exception {
        CommandBuilder commandBuilderMocked = Mockito.mock(CommandBuilder.class);

        Mockito.when(commandBuilderMocked.executeOn(anyString())).thenReturn(output);

        Mockito.when(commandBuilderMocked.put(anyString(), anyString())).thenReturn(commandBuilderMocked);
        PowerMockito.whenNew(CommandBuilder.class).withAnyArguments().thenReturn(commandBuilderMocked);
        return commandBuilderMocked;
    }

    public static void mockCommandBuilderDownloadSpeakerInfo(String output) throws Exception {
        PowerMockito.mockStatic(CommandBuilder.class);
        PowerMockito.when(CommandBuilder.download(anyString(), anyString())).thenReturn(output);
    }
}
