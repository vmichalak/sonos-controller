.. _getting_started:

Getting started
===============



.. _installation:

Installation
------------

.. _from_git:

From git
^^^^^^^^

To use the library, you can clone the git repo directly.

.. code-block:: shell

    git clone https://github.com/vmichalak/sonos-controller.git
    cd sonos-controller
    git submodule init
    git submodule update

.. _tutorial:

Tutorial
--------

Discover your devices
^^^^^^^^^^^^^^^^^^^^^

For discovering the Sonos devices in your network, use ``SonosDiscovery`` object.

.. code-block:: java

    List<SonosDevice> devices = SonosDiscovery.discover();
    SonosDevice device = devices.get(0);

.. code-block:: java

    SonosDevice device = SonosDiscovery.discoverOne();

If you know the ip address of your SONOS speaker, you also can instantiate a ``SonosDevice`` directly.

.. code-block:: java

    SonosDevice device = new SonosDevice("10.0.0.102");


Control your devices
^^^^^^^^^^^^^^^^^^^^

You can use the ``SonosDevice`` instance to interact with your SONOS speaker.

.. code-block:: java

    device.getCurrentTrackInfo();
    device.pause();
    device.setVolume(15);
    device.getVolume();


