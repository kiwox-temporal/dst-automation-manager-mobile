package net.kiwox.manager.dst.service.interfaces;

import net.kiwox.manager.dst.domain.TestControllerProbe;

import java.util.List;

public interface ITestControllerProbeService {

    List<TestControllerProbe> findAllTestControllerProbe();
    
    List<TestControllerProbe> findByControllerName(String controllerName);
    
}
