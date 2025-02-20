package com.am.design.development.utilities.facade;

import java.io.File;
import java.io.IOException;

public interface RemoteRetrieverFacade {

    File downloadVideo(String videoUrl) throws IOException;
}
