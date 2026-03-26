<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Rust Enhanced Syntax Highlighting Changelog

## [Unreleased]

## [0.0.2]
### Added
- Async call highlighting: `.await` expressions, `async fn` call sites, `async`/`async move` blocks, spawn calls (`tokio::spawn`, `async_std::task::spawn`, etc.)
- Early return highlighting: `return` statements, `?` operator, `.unwrap()`/`.expect()` calls, panic macros (`panic!`, `unreachable!`, `unimplemented!`, `todo!`)
- Hourglass gutter icon for async calls
- Return arrow gutter icon for early returns
- Inlay hints (`async`, `spawn`) for non-obvious async calls
- Inline border highlighting (ROUNDED_BOX) with theme-adaptive colors
- Per-category toggles for gutter icons and inline highlighting
- Test code detection: early return highlighting disabled by default in `#[test]` functions, `#[cfg(test)]` modules, and `tests/` directories
- Color customization via Settings > Editor > Color Scheme
- Settings page at Settings > Tools > Rust Enhanced Syntax Highlighting

## [0.0.1]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
