<div align="center">

# Kotich

**A beautiful manga reader for Android with an iOS 27 "Liquid Glass" design language.**

Forked from [Kotatsu](https://github.com/KotatsuApp/Kotatsu) — redesigned from the ground up with Apple's Liquid Glass aesthetic.

![Android 6.0](https://img.shields.io/badge/android-6.0+-brightgreen) [![License](https://img.shields.io/github/license/KotatsuApp/Kotatsu)](https://github.com/KotatsuApp/Kotatsu/blob/devel/LICENSE)

</div>

---

## About

Kotich is a free and open-source manga reader for Android that brings the **iOS 27 Liquid Glass** design philosophy to the manga reading experience. Built on top of the powerful Kotatsu engine with 1200+ manga sources, Kotich reimagines every screen with frosted glass surfaces, vibrant system colors, and the clean, breathable layouts inspired by Apple's latest design language.

## Design Philosophy — iOS 27 "Liquid Glass"

Kotich implements a genuine **Liquid Glass** design system inspired by Apple's iOS 26/27 aesthetic:

- **Frosted Glass Surfaces** — Translucent cards and sheets with subtle blue tint refraction
- **Vibrant System Colors** — iOS Blue (#007AFF), Green (#34C759), Purple (#AF52DE), and more
- **Clean Backgrounds** — Classic iOS light gray (#F2F2F7) / true black dark mode (#000000)
- **SF Pro Typography** — System sans-serif with light/regular/medium/bold weight hierarchy
- **16dp Corner Radius** — iOS-standard rounded cards, 12dp buttons, 20dp sheets
- **Subtle Shadows** — Soft elevation instead of Material elevation
- **Large Title Navigation** — iOS-style collapsible large titles
- **Bottom Sheets with Drag Handles** — iOS-pattern modal sheets
- **Pill-shaped Buttons** — Rounded action buttons following iOS conventions
- **Generous Whitespace** — Breathing room between elements for a premium feel

### Design Files

The Liquid Glass design system is defined across several resource files:

| File | Purpose |
|------|---------|
| `colors_ios27.xml` | Complete iOS 27 color palette (system colors, glass tints, surfaces) |
| `themes.xml` → `Theme.Kotich.iOS27` | Full Material 3 theme mapped to iOS colors |
| `styles.xml` → `Widget.Kotich.iOS.*` | iOS-style widgets (cards, buttons, chips, search bar, sheets) |
| `drawable/bg_glass_card.xml` | Frosted glass card backgrounds |
| `drawable/bg_ios_button.xml` | iOS pill button backgrounds |
| `drawable/bg_ios_bottom_sheet.xml` | iOS bottom sheet with rounded top corners |
| `drawable/ic_ios_*.xml` | SF Symbols-style vector icons |

## Main Features

<div align="left">

* Online [manga catalogues](https://github.com/KotatsuApp/kotatsu-parsers) (with 1200+ manga sources)
* Search manga by name, genres and more filters
* Favorites organized by user-defined categories
* Reading history, bookmarks and incognito mode support
* Download manga and read it offline. Third-party CBZ archives are also supported
* **iOS 27 Liquid Glass UI** — frosted glass cards, vibrant colors, generous spacing
* Standard and Webtoon-optimized customizable reader, gesture support on reading interface
* Notifications about new chapters with updates feed, manga recommendations (with filters)
* Integration with manga tracking services: Shikimori, AniList, MyAnimeList, Kitsu
* Password / fingerprint-protected access to the app
* Automatically sync app data with other devices on the same account
* Support for older devices running Android 6.0+

</div>

### In-App Screenshots

<div align="center">
    <img src="./metadata/en-US/images/phoneScreenshots/1.png" alt="Mobile view" width="250"/>
    <img src="./metadata/en-US/images/phoneScreenshots/2.png" alt="Mobile view" width="250"/>
    <img src="./metadata/en-US/images/phoneScreenshots/3.png" alt="Mobile view" width="250"/>
    <img src="./metadata/en-US/images/phoneScreenshots/4.png" alt="Mobile view" width="250"/>
    <img src="./metadata/en-US/images/phoneScreenshots/5.png" alt="Mobile view" width="250"/>
    <img src="./metadata/en-US/images/phoneScreenshots/6.png" alt="Mobile view" width="250"/>
</div>

<br>

<div align="center">
    <img src="./metadata/en-US/images/tenInchScreenshots/1.png" alt="Tablet view" width="400"/>
    <img src="./metadata/en-US/images/tenInchScreenshots/2.png" alt="Tablet view" width="400"/>
</div>

### Localization

<a href="https://hosted.weblate.org/engage/kotatsu/">
<img src="https://hosted.weblate.org/widget/kotatsu/horizontal-auto.png" alt="Translation status" />
</a>

**Kotich is localized in a number of different languages.**<br>
**📌 If you would like to help improve these or add new languages,
please head over to the [Weblate project page](https://hosted.weblate.org/engage/kotatsu/)**

### Contributing

Pull requests are welcome. See [CONTRIBUTING.md](https://github.com/KotatsuApp/Kotatsu/blob/devel/CONTRIBUTING.md) for guidelines.

### Certificate fingerprints

```plaintext
2C:19:C7:E8:07:61:2B:8E:94:51:1B:FD:72:67:07:64:5D:C2:58:AE
```

```plaintext
67:E1:51:00:BB:80:93:01:78:3E:DC:B6:34:8F:A3:BB:F8:30:34:D9:1E:62:86:8A:91:05:3D:BD:70:DB:3F:18
```

### License

[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)

<div align="left">

You may copy, distribute and modify the software as long as you track changes/dates in source files. Any modifications
to or software including (via compiler) GPL-licensed code must also be made available under the GPL along with build &
install instructions.

</div>

### DMCA disclaimer

<div align="left">

The developers of this application do not have any affiliation with the content available in the app and does not store
or distribute any content. This application should be considered a web browser, all content that can be found using this
application is freely available on the Internet. All DMCA takedown requests should be sent to the owners of the website
where the content is hosted.

</div>
