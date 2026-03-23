# Rust Enhanced Syntax Highlighting

![Build](https://github.com/squadgazzz/rust-syntax-tools-rustrover-plugin/workflows/Build/badge.svg)

<!-- Plugin description -->
Enhances Rust syntax highlighting in RustRover with visual indicators for async calls
(.await, async fn, spawn) and early return points (return, ?, unwrap, panic). Features
gutter icons, inline border highlighting, and inlay hints — each independently configurable.
<!-- Plugin description end -->

## Features

### Async Call Highlighting

Detects and highlights async-related code with an hourglass gutter icon and colored borders:

- `.await` expressions
- `async fn` call sites
- `async` / `async move` blocks
- `tokio::spawn`, `async_std::task::spawn`, and other spawn functions
- Inlay hints (`async`, `spawn`) for non-obvious async calls

### Early Return Highlighting

Detects and highlights early exit points with a return arrow gutter icon and colored borders:

- `return` statements
- `?` operator
- `.unwrap()` / `.expect()` calls
- `panic!()`, `unreachable!()`, `unimplemented!()`, `todo!()` macros
- Disabled in test code by default (configurable)

### Configuration

All features can be independently toggled in **Settings > Tools > Rust Enhanced Syntax Highlighting**:

- Per-category gutter icon toggles
- Per-category inline border toggles
- Inlay hint toggle
- Test code exclusion for early returns

Colors are customizable via **Settings > Editor > Color Scheme > Rust Enhanced Syntax Highlighting**.

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Rust Enhanced Syntax Highlighting"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/squadgazzz/rust-syntax-tools-rustrover-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
