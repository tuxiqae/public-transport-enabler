Welcome to Public Transport Enabler, a Java library allowing you to get data from public transport providers.

This project contains several subprojects:

 * [__enabler__](enabler):
     The library itself. This is probably what you're searching for. See the subproject's [README](enabler/README.md) for more information.
 * [__service__](service):
     An example of how the library could be used as a web service. It's still very incomplete though.

You can build all sub-projects at once using Gradle:

`gradle clean build`

---

**THIS IS AN UNOFFICIAL FORK**: It is kept in sync with the origin from
[github.com/schildbach/public-transport-enabler](https://github.com/schildbach/public-transport-enabler) and applies selected pull requests until they are merged into upstream:

* [Incofer, Costa Rica](https://github.com/schildbach/public-transport-enabler/pull/146)
  * `NetworkId.CR`
  * [CostaRicaProvider.java](enabler/src/de/schildbach/pte/CostaRicaProvider.java)
  * [CostaRicaProviderLiveTest.java](enabler/test/de/schildbach/pte/live/CostaRicaProviderLiveTest.java)
* [New York, United States](https://github.com/schildbach/public-transport-enabler/pull/97)
  * `NetworkId.NEWYORK`
  * [NewyorkProvider.java](enabler/src/de/schildbach/pte/NewyorkProvider.java)
  * [NewyorkProviderLiveTest.java](enabler/test/de/schildbach/pte/live/NewyorkProviderLiveTest.java)
* [Brazil](https://github.com/schildbach/public-transport-enabler/pull/179)
  * `NetworkId.BR`
  * [BrProvider.java](enabler/src/de/schildbach/pte/BrProvider.java)
  * [BrProviderLiveTest.java](enabler/test/de/schildbach/pte/live/BrProviderLiveTest.java)
  * `NetworkId.BRFLORIPA`
  * [BrFloripaProvider.java](enabler/src/de/schildbach/pte/BrFloripaProvider.java)
  * [BrFloripaProviderLiveTest.java](enabler/test/de/schildbach/pte/live/BrFloripaProviderLiveTest.java)
* [California, United States](https://github.com/schildbach/public-transport-enabler/pull/164)
  * `NetworkId.CALIFORNIA`
  * [CaliforniaProvider.java](enabler/src/de/schildbach/pte/CaliforniaProvider.java)
  * [CaliforniaProviderLiveTest.java](enabler/test/de/schildbach/pte/live/CaliforniaProviderLiveTest.java)
* [Hungary](https://github.com/schildbach/public-transport-enabler/pull/195)
  * `NetworkId.HUNGARY`
  * [HungaryProvider.java](enabler/src/de/schildbach/pte/HungaryProvider.java)
  * [HungaryProviderLiveTest.java](enabler/test/de/schildbach/pte/live/HungaryProviderLiveTest.java)
* [HSL using Navitia](https://github.com/schildbach/public-transport-enabler/pull/208)
  * `NetworkId.FI`
  * [HslProvider.java](enabler/src/de/schildbach/pte/HslProvider.java)
  * [HslProviderLiveTest.java](enabler/test/de/schildbach/pte/live/HslProviderLiveTest.java)
* [Portugal](https://github.com/schildbach/public-transport-enabler/pull/244)
  * `NetworkId.PT`
  * [PortugalProvider.java](enabler/src/de/schildbach/pte/PortugalProvider.java)
  * [PortugalProviderLiveTest.java](enabler/test/de/schildbach/pte/live/PortugalProviderLiveTest.java)

There is a [mirror on github](https://github.com/opentransitmap/public-transport-enabler/), but for merge request please refer to our [Gitlab repository](https://gitlab.com/opentransitmap/public-transport-enabler).

