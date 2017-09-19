# Atreus Keyboard Layout Editor

A web GUI for creating/editing layouts for the [Atreus] keyboard.

[Atreus]: https://atreus.technomancy.us

## Setup

### Development

Run the development server with auto-compilation and auto-testing:

```
boot dev
```

See the results in your browser:

- App: [http://localhost:8080](http://localhost:8080)
- Devcards: [http://localhost:8080/devcards](http://localhost:8080/devcards)

You should see each update automatically if any HTML, styles or
ClojureScript files are changed on disk.

The `dev` build will start an `nREPL` server on a random port (which
it will print out). If you connect to that (with say `boot repl -c`),
and then run:


``` clojure
boot.user=> (start-repl)
```

Once you visit http://localhost:8080 you will have a browser-connected
ClojureScript REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.


### Release

Compile once using minification:

```
boot release
```

Assets will be in the `target` directory.

## License

Copyright © 2017 Geoff Shannon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
