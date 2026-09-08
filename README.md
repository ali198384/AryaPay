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

ورود (OTP دمو + PIN + Biometric)

→ خانه

→ انتقال کارت / شبا / موبایل     ← قلب پورتفولیو

→ پیش‌نمایش کارمزد → تأیید → OTP → رسید

→ تاریخچه صفحه‌بندی‌شده

→ تنظیمات امنیتی

قبض، اسکن کارت و سخت‌سازی production (Pinning، R8، screenshot) بعد از تکمیل انتقال می‌آیند.

---

## معماری

الگوی رسمی Android + UDF:

```text
Compose Screen
      ↓
   UiIntent
      ↓
  ViewModel
      ↓
UseCase (:core:domain)   ← JVM-only
      ↓
 Repository
      ↓
Room / DataStore / FakeApi
      ↓
StateFlow<UiState>
SharedFlow<UiEffect>
```

جملهٔ کوتاه:

> معماری رسمی اندروید با UDF و offline-first. Domain جدا است چون قوانین ریال و شبا باید بدون Android تست شوند.

### قوانین غیرقابل مذاکره

1. `:core:domain` به Android / Retrofit / Room / Compose وابسته نیست.
2. فیچرها به هم dependency ندارند. Nav host فقط در `:app`.
3. مبلغ فقط `Rial` از جنس `Long`. Detekt استفاده از `Double`/`Float` برای پول را رد می‌کند.
4. PAN کامل، CVV، PIN، OTP در لاگ، Room یا Analytics نمی‌روند.
5. Biometric، Clock، RNG و RootDetector پشت اینترفیس‌اند تا Fake شوند.
6. هر صفحهٔ P0 چهار حالت دارد: Loading / Empty / Success / Error.
7. تومان و اعداد فارسی فقط در UI؛ دامنه با ریال کار می‌کند.

جزئیات تصمیم‌ها: [`docs/adr/`](docs/adr/).

---

## ماژول‌ها

**Package:** `ir.aryapay`

```text
app/                    Nav host, Hilt, flavors

build-logic/            convention plugins

core/
├── common/              Result, AppError, DispatcherProvider
├── domain/              مدل‌ها، UseCase، اینترفیس Repository
├── data/                پیاده‌سازی Repository
├── database/            Room
├── network/             Retrofit + Fake
├── datastore/           DataStore
├── security/            Security abstractions
├── ui/                  Design system، RTL، اعداد فارسی
└── testing/             Fakeها و TestDispatcher

feature/
├── auth/
├── home/
├── wallet/
├── transfer/
├── transactions/
└── profile/

bills/  cards/           ← بعد از تکمیل انتقال
```

---

## استک

| لایه  | انتخاب                                                              |
| ----- | ------------------------------------------------------------------- |
| UI    | Compose, Material 3, Navigation 2 type-safe                         |
| DI    | Hilt                                                                |
| داده  | Room (source of truth), DataStore, Retrofit + Kotlinx Serialization |
| تست   | JUnit 5, Turbine, Fake, MockWebServer, Paparazzi (بعدی)             |
| کیفیت | Detekt, Spotless, GitHub Actions, LeakCanary (debug)                |

---

## اجرا

Android Studio با Kotlin 2 / AGP فعلی، JDK 17، امولاتور API 26+.

```bash
git clone https://github.com/ali198384/AryaPay.git

cd AryaPay

./gradlew test

./gradlew installDemoDebug
```

Flavor پیش‌فرض: `demo` — Fake API، بدون شبکهٔ بانکی.

---

## وضعیت

**M0 — قرارداد و اسکلت.** فیچر مالی هنوز پیاده نشده.

| مایل‌استون | خروجی                                                                |
| ---------- | -------------------------------------------------------------------- |
| M1         | Catalog، convention plugins، `Rial`، validators ایران، اولین تست JVM |
| M2         | UseCaseهای انتقال + تست جدول‌محور                                    |
| M3         | PIN / Biometric / قفل اپ + CI                                        |
| M4         | کیف پول offline-first                                                |
| M5         | انتقال کامل + رسید — قلب رزومه                                       |
| M6–M8      | قبض، سخت‌سازی production، تکمیل حالت‌های UI                          |

---

## امنیت و محدوده

* کارت، CVV، PIN یا OTP واقعی ذخیره یا لاگ نمی‌شود.
* به شاپرک، فینوتک یا بانک وصل نیست.
* برای مصاحبه و یادگیری معماری است، نه استفادهٔ تولیدی.
* نام یا برند بانک ایرانی استفاده نشده است.
