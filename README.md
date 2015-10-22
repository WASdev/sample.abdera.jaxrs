# Java EE7: JAX-RS 2.0 Integrate with Apache Abdera Atom

JAX-RS 2.0 added Atom support using Apache Abdera.


Register Apache Abdera based Feed and/or Entry providers (net.wasdev.wlp.sample.abdera.jaxrs.atom.AtomFeedProvider and net.wasdev.wlp.sample.abdera.jaxrs.atom.AtomEntryProvider) with a jaxrs endpoint and have resource methods explicitly dealing with Abdera Feed or Entry classes. This is the most advanced option in that it lets users build Feeds or Entries in the way which suits most. Note that Abdera has not been actively mantained recently but practically speaking it is very good for working with most of the cases one may have to deal with when developing Atom-based applications.

Both AtomFeedProvider and AtomEntryProvider support a 'formattedOutput' (pretty-printing) property.
## Getting Started

Browse the code to see what it does, or build and run it yourself:
* [Building and running on the command line](/docs/Using-cmd-line.md)
* [Building and running using Eclipse and WebSphere Development Tools (WDT)](/docs/Using-WDT.md)
* [Downloading WAS Liberty](/docs/Downloading-WAS-Liberty.md)

Once the server has been started, go to [http://localhost:9081/abdera-jaxrs-application/](http://localhost:9081/abdera-jaxrs-application/) to interact with the sample.


## More on JAX-RS 2.0 and related technologies
* [JSR 339: JAX-RS 2.0](https://jcp.org/en/jsr/detail?id=339)
* [JSR 166: Concurrency Utilities](https://jcp.org/en/jsr/detail?id=166)
* [JSR 345: Enterprise JavaBeansTM 3.2](https://jcp.org/en/jsr/detail?id=345)

## Notice

© Copyright IBM Corporation 2015.

## License

```text
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````
