# AryaPay

نئوبانک دمو برای مصاحبه Senior Android — معماری، مدل پول، امنیت لایهٔ اپ، و تست.

این اپ به بانک واقعی وصل نیست. داده و API کاملاً نمایشی‌اند.

`minSdk 26` · Kotlin 2.x · Compose · Hilt · UDF/MVI · Offline-first

---

## این ریپو چه چیزی را نشان می‌دهد

- مدل پول با `value class Rial(val value: Long)` — بدون `Double`/`Float`
- دامنهٔ خالص JVM؛ قوانین کسب‌وکار بدون امولاتور تست می‌شوند
- ماژولار واقعی: `:feature:*` همدیگر را نمی‌بینند
- Offline-first: Room منبع حقیقت است، شبکه فقط refresh
- امنیت به‌صورت لایه (Keystore، Biometric، redaction) نه `if` پراکنده
- CI روی PR: تست + Detekt + ممنوعیت مبلغ اعشاری

---

## جریان محصول
# AryaPay

پروژهٔ نمونهٔ یک اپلیکیشن فین‌تک اندرویدی با تمرکز بر معماری تمیز، امنیت، تست‌پذیری و رویکرد `offline-first`.

این پروژه با هدف **یادگیری عمیق معماری Android و ساخت یک پورتفولیوی حرفه‌ای برای مصاحبه‌های شغلی** توسعه داده می‌شود.

---

## جریان محصول

```text
ورود
 ├── OTP دمو
 ├── PIN
 └── Biometric
       ↓
     خانه
       ↓
انتقال وجه
 ├── کارت
 ├── شبا
 └── موبایل
       ↓
پیش‌نمایش کارمزد
       ↓
     تأیید
       ↓
      OTP
       ↓
     رسید
       ↓
تاریخچهٔ صفحه‌بندی‌شده
       ↓
تنظیمات امنیتی
```

**انتقال وجه قلب اصلی پورتفولیو است.**

قابلیت‌های قبض، اسکن کارت و سخت‌سازی Production مانند `Pinning`، `R8` و جلوگیری از `Screenshot` بعد از تکمیل جریان انتقال اضافه خواهند شد.

---

## معماری

الگوی اصلی پروژه:

**Android Architecture + UDF + Offline-first**

```text
Compose Screen
      ↓
   UiIntent
      ↓
  ViewModel
      ↓
UseCase
(:core:domain)
      ↓
 Repository
      ↓
Room / DataStore / FakeApi
      ↓
StateFlow<UiState>
      +
SharedFlow<UiEffect>
```

### جملهٔ کوتاه برای مصاحبه

> معماری رسمی Android را با UDF و رویکرد offline-first پیاده‌سازی کرده‌ام.
> لایهٔ Domain را مستقل نگه داشته‌ام تا قوانین مالی مثل ریال و شبا بدون وابستگی به Android قابل تست باشند.

---

## قوانین غیرقابل مذاکره

1. ماژول `:core:domain` به Android، Retrofit، Room یا Compose وابستگی ندارد.
2. Featureها به یکدیگر Dependency ندارند.
3. `NavHost` فقط در ماژول `:app` قرار دارد.
4. مبلغ در کل Domain فقط با `Rial` و از نوع `Long` نگهداری می‌شود.
5. استفاده از `Double` و `Float` برای پول توسط `Detekt` رد می‌شود.
6. `PAN` کامل، `CVV`، `PIN` و `OTP` در Log، Room یا Analytics ذخیره نمی‌شوند.
7. `Biometric`، `Clock`، `RNG` و `RootDetector` پشت Interface قرار دارند تا قابل Fake شدن باشند.
8. هر صفحهٔ P0 چهار وضعیت اصلی دارد:

```text
Loading
Empty
Success
Error
```

9. تبدیل تومان و نمایش اعداد فارسی فقط در UI انجام می‌شود.
10. Domain فقط با ریال کار می‌کند.

جزئیات تصمیم‌های معماری در مسیر زیر قرار دارند:

```text
docs/adr/
```

---

## ماژول‌ها

Package اصلی:

```text
ir.aryapay
```

ساختار پروژه:

```text
AryaPay
│
├── app/
│   └── NavHost, Hilt, Flavors
│
├── build-logic/
│   └── Convention Plugins
│
├── core/
│   │
│   ├── common/
│   │   └── Result, AppError, DispatcherProvider
│   │
│   ├── domain/
│   │   └── Models, UseCases, Repository Interfaces
│   │
│   ├── data/
│   │   └── Repository Implementations
│   │
│   ├── database/
│   │   └── Room
│   │
│   ├── network/
│   │   └── Retrofit + FakeApi
│   │
│   ├── datastore/
│   │   └── DataStore
│   │
│   ├── security/
│   │   └── Security Abstractions
│   │
│   ├── ui/
│   │   └── Design System, RTL, Persian Numbers
│   │
│   └── testing/
│       └── Fakes, TestDispatcher
│
└── feature/
    │
    ├── auth/
    ├── home/
    ├── wallet/
    ├── transfer/
    ├── transactions/
    └── profile/
```

Featureهای زیر بعد از تکمیل جریان انتقال اضافه خواهند شد:

```text
feature/
├── bills/
└── cards/
```

---

## استک

| حوزه                  | تکنولوژی                         |
| --------------------- | -------------------------------- |
| UI                    | Jetpack Compose + Material 3     |
| Navigation            | Navigation 2 با Type-safe Routes |
| DI                    | Hilt                             |
| Database              | Room                             |
| Preferences           | DataStore                        |
| Network               | Retrofit                         |
| Serialization         | Kotlinx Serialization            |
| Architecture          | Clean Architecture + UDF         |
| State                 | StateFlow                        |
| Events                | SharedFlow                       |
| Testing               | JUnit 5 + Turbine + Fake         |
| Network Testing       | MockWebServer                    |
| Screenshot Testing    | Paparazzi                        |
| Code Quality          | Detekt + Spotless                |
| CI/CD                 | GitHub Actions                   |
| Memory Leak Detection | LeakCanary در Debug              |

---

## اجرا

### پیش‌نیازها

```text
Android Studio
Kotlin 2.x
AGP فعلی پروژه
JDK 17
Android Emulator API 26+
```

### دریافت پروژه

```bash
git clone https://github.com/<USER>/AryaPay.git
cd AryaPay
```

### اجرای تست‌ها

```bash
./gradlew test
```

### اجرای نسخهٔ Demo

```bash
./gradlew installDemoDebug
```

### Demo Flavor

Flavor پیش‌فرض پروژه:

```text
demo
```

نسخهٔ Demo از `FakeApi` استفاده می‌کند و به هیچ شبکهٔ بانکی واقعی متصل نیست.

---

## وضعیت پروژه

**M0 — قرارداد و اسکلت**

در این مرحله Feature مالی هنوز پیاده‌سازی نشده و تمرکز روی زیرساخت، قراردادهای معماری و آماده‌سازی پروژه است.

| Milestone | خروجی                                                                 |
| --------- | --------------------------------------------------------------------- |
| M0        | قرارداد معماری و اسکلت پروژه                                          |
| M1        | Catalog، Convention Plugins، `Rial`، Validators ایران و اولین تست JVM |
| M2        | UseCaseهای انتقال و تست‌های جدول‌محور                                 |
| M3        | PIN، Biometric، قفل اپ و CI                                           |
| M4        | کیف پول Offline-first                                                 |
| M5        | انتقال کامل + رسید؛ قلب رزومه                                         |
| M6        | قبض                                                                   |
| M7        | سخت‌سازی Production                                                   |
| M8        | تکمیل حالت‌های UI و بهبود نهایی                                       |

---

## امنیت و محدوده

* اطلاعات واقعی کارت، `CVV`، `PIN` و `OTP` ذخیره یا Log نمی‌شوند.
* پروژه به شاپرک، فینوتک یا هیچ بانک واقعی متصل نیست.
* APIهای بانکی با `FakeApi` شبیه‌سازی می‌شوند.
* این پروژه برای **مصاحبه، یادگیری و نمایش توانایی‌های معماری** ساخته شده است.
* پروژه برای استفادهٔ Production یا انجام تراکنش مالی واقعی طراحی نشده است.
* نام یا برند هیچ بانک ایرانی در پروژه استفاده نشده است.

---

## اهداف پروژه

تمرکز اصلی AryaPay روی موارد زیر است:

```text
Clean Architecture
        +
UDF / Unidirectional Data Flow
        +
Offline-first
        +
Testable Domain
        +
Financial Data Safety
        +
Modern Android Development
        +
CI / Code Quality
```

هدف این پروژه صرفاً ساخت یک UI بانکی نیست؛ بلکه نمایش توانایی طراحی و پیاده‌سازی یک **سیستم اندرویدی قابل نگهداری، تست‌پذیر و آمادهٔ توسعه** است.
