# AryaPay

سوپراپ فین‌تک دمو برای مصاحبه **Senior Android**.

نئوبانک آموزشی — نه بانک واقعی. هدف این ریپو نمایش معماری، مدل پول، امنیت لایهٔ اپ، تست و تصمیم‌گیری است؛ نه اتصال زنده به شبکهٔ بانکی.

minSdk 26 · Kotlin 2.x · Jetpack Compose · Hilt · Clean Architecture (UDF/MVI) · Offline-first

---

## چرا این پروژه

رزومهٔ من تجربهٔ محصول و رهبری تیم را نشان می‌دهد. این ریپو چیزهایی را نشان می‌دهد که در مصاحبه Senior فین‌تک واقعاً پرسیده می‌شود:

- مدل پول بدون `Double`/`Float`
- دامنهٔ خالص JVM و قوانین کسب‌وکار قابل اثبات با تست
- ماژولار واقعی (فیچرها همدیگر را نمی‌بینند)
- امنیت به‌صورت لایه، نه `if` پراکنده
- Offline-first برای موجودی و تاریخچه
- CI که PR خراب را نگه می‌دارد
- ADR برای هر تصمیم معماری

KMP، Wear، ویجت و Navigation 3 عمداً در نسخهٔ ۱ نیستند. امتیاز Senior از عمق P0 می‌آید، نه از تعداد صفحه.

---

## محصول نسخه ۱
ورود (OTP دمو + PIN + Biometric)

→ خانه (موجودی + میانبر + Loading/Empty/Error)

→ انتقال کارت / شبا / موبایل ← قلب پورتفولیو

→ پیش‌نمایش کارمزد → تأیید → OTP → رسید

→ تاریخچه صفحه‌بندی‌شده + فیلتر

→ تنظیمات امنیتی (قفل، timeout، biometrics)


اولویت	چیست	قانون
P0	اسکلت ماژولار، مدل پول، Auth/قفل، کیف پول، انتقال، رسید، تاریخچه، امنیت، تست، CI، ADR، flavor دمو	بدون DoD سبز، فیچر بعدی شروع نمی‌شود
P1	قبض/شارژ، اسکن کارت، SSL Pinning، Root detection، R8 full، screenshot test	فقط بعد از تکمیل P0
P2	داشبورد هزینه، ویدیوی دمو، GitHub Release امضاشده	تمایز؛ هویت پروژه نیست
نسخهٔ ۱ ندارد: بک‌اند واقعی بانک، فینوتک live، بورس، چت، KMP.

APIها Fake تمیز با قرارداد مشخص هستند. اگر sandbox آمد، پشت همان اینترفیس می‌رود.

معماری
الگوی رسمی Guide to app architecture و Now in Android:

UI: Compose + ViewModel + UDF (UiState / UiIntent / UiEffect)
Domain: UseCaseهایی که قانون واقعی دارند، نه wrapper خالی
Data: Repository = تنها درگاه؛ Room منبع حقیقت؛ شبکه برای refresh
Screen (Compose)

→ Intent

→ ViewModel

→ UseCase (:core:domain) خالص JVM

→ Repository (interface)

→ Repository impl (:core:data)

→ Room / DataStore / FakeApi

→ StateFlow<UiState>

→ SharedFlow<UiEffect>

جملهٔ مصاحبه:

معماری رسمی اندروید با UDF و offline-first. Domain جدا است چون قوانین پول و شبا باید بدون Android تست شوند.

قوانین غیرقابل مذاکره
:core:domain هیچ dependency به Android / Retrofit / Room / Compose ندارد.
:feature:* همدیگر را نمی‌بینند. گراف ناوبری فقط در :app.
پول فقط value class Rial(val value: Long). Double/Float برای مبلغ compile-time ممنوع (Detekt).
PAN کامل، CVV، PIN، OTP در لاگ، Room یا Analytics نمی‌روند.
Biometric، Clock، RNG، RootDetector پشت اینترفیس می‌روند تا Fake شوند.
هیچ فیچر P0 بدون تست قانون کسب‌وکار merge نمی‌شود.
هر صفحهٔ P0 چهار حالت دارد: Loading / Empty / Success / Error.
تومان و اعداد فارسی فقط در UI؛ دامنه با ریال (Long) کار می‌کند.
ماژول‌ها
Package ریشه: ir.aryapay

AryaPay/

├── README.md

├── docs/adr/ # تصمیم‌های معماری

├── gradle/libs.versions.toml

├── build-logic/ # convention plugins

├── app/ # Nav host, HiltApp, flavors

├── core/

│ ├── common/ # Result, AppError, DispatcherProvider

│ ├── domain/ # مدل‌ها، UseCase، اینترفیس Repository

│ ├── data/ # impl ریپو، mapper

│ ├── database/ # Room

│ ├── network/ # Retrofit + Fake (+ Pinning در P1)

│ ├── datastore/

│ ├── security/

│ ├── ui/ # Design system، RTL، اعداد فارسی

│ └── testing/ # Fakeها، TestDispatcher، faker شناسه‌ها

└── feature/

├── auth/

├── home/

├── wallet/

├── transfer/ # مهم‌ترین ماژول رزومه

├── transactions/

├── bills/ # P1

├── cards/ # P1

└── profile/

Tech stack

انتخاب	چرا
Kotlin 2.x + Compose + Material 3	استاندارد فعلی
minSdk 26	Biometric + EncryptedFile بدون دردسر
Hilt	بازار ایران؛ تست‌پذیر با @UninstallModules
Navigation 2 type-safe + kotlinx.serialization	شناخته‌شده در مصاحبه‌های ایران
Room منبع حقیقت + DataStore برای prefs	offline-first واقعی
Retrofit + OkHttp + Kotlinx Serialization	لایهٔ شبکه
JUnit 5 + Turbine + Fake	هرم تست
MockWebServer	قرارداد API
Paparazzi	screenshot بدون امولاتور (P1)
GitHub Actions + Detekt + Spotless	کیفیت اجباری
LeakCanary فقط debug	production mindset
Navigation 3 عمداً انتخاب نشد. امتیاز از type-safe بودن و جدا بودن گراف فیچر است، نه از شماره نسخه. دلیل در docs/adr/ ثبت می‌شود.

XML جدید نوشته نمی‌شود.

Definition of Done
فیچر P0 تمام نیست مگر:

[ ] قانون دامنه با Unit Test سبز است
[ ] ViewModel: Intent → State / Effect تست شده
[ ] Loading / Empty / Error / Success در UI هست
[ ] اعداد فارسی، RTL؛ تومان فقط در UI
[ ] هیچ secretای در لاگ نیست
[ ] فیچر به فیچر دیگر dependency ندارد
[ ] در صورت تصمیم معماری: ADR
[ ] CI روی PR سبز است
مایل‌استون‌ها
بدون تقویم. هر مایل‌استون وقتی تمام است که روی امولاتور و روی تست‌ها قابل دفاع باشد.


خروجی	سیگنال استخدام
M0	قرارداد + ریپوی خالی	همین فایل
M1	Catalog، convention plugins، تم RTL، Rial، validators ایران، Detekt، اولین تست JVM	./gradlew test بدون دستگاه
M2	UseCaseهای انتقال، FakeRepository، تست جدول‌محور	سقف روزانه را با تست نشان می‌دهم
M3	OTP دمو، PIN، Biometric، Encrypted DataStore، قفل timeout، CI: test + detekt	امنیت لایه است، نه پراکنده
M4	Room، موجودی، تاریخچه، pagination	منبع حقیقت لوکال است
M5	State machine انتقال، رسید بانکی، MockWebServer	قلب رزومه؛ اگر فقط تا اینجا عالی باشد برای Senior فین‌تک کافی است
M6	قبض/شارژ + اسکن کارت	فقط بعد از DoD کامل M5
M7	Pinning، Root/Emulator detection، FLAG_SECURE، R8، screenshot، Baseline Profile	سخت‌سازی production
M8	README دفاعی، نمودار ماژول، جدول ADR، ویدیوی ۹۰ ثانیه‌ای	بستهٔ مصاحبه
تست
/\

/UI\ کم؛ Paparazzi + مسیرهای حیاتی Compose

/----\

/ VM \ Intent → State برای هر صفحه P0

/ Data \ Room in-memory + MockWebServer

/-----------\

/ Domain Unit \ زیاد؛ جدول قوانین مالی و شناسه‌ها

Fake بر مالی و شناسه‌ها

Fake بر Mock خارجی پیچیده است.

جملهٔ مصاحبه:

CIم تست شکسته یا Double برای ریال را روی main بگذارم.

ADR
تصمیم‌ها در docs/adr/:

چرا Rial و چرا Long
چرا فیچرها همدیگر را نمی‌بین. چرا Room منبع حقیقت است نه پاسخ شبکه4. چرا Navigation 2 type-safe
چرا Room منبع حقیقت است نه پاسخ شبکه
چرا PAN کامل هرگز persist نمی‌شود
هر فایل چند خط است. هدف: تیم بعدی دلیل را حدس نزند.

اجرا
پیش‌نیاز: Android Studio مناسب Kotlin 2 / AGP فعلی، JDK 17، امولاتور API 26+.

bash
git clone https://github.com/ali198384/AryaPay.git
cd AryaPay
./gradlew test          # تست‌های JVM؛ از M1
./gradlew installDemoDebug
Flavor پیش‌فرض: demo (Fake API، بدون شبکهٔ بانکی).

وضعیت فعلی: M0 — قرارداد و اسکلت استودیو. فیچر مالی هنوز پیاده نشده است.

امنیت و محدودهٔ دمو
هیچ کارت، CVV، PIN یا OTP واقعی ذخیره یا لاگ نمی‌شود.
PAN در UI ماسک می‌شود؛ در دامنه حداکثر به‌صورت hashed/tokenized.
این اپ به بانک، شاپرک یا فینوتک وصل نیست. دادهٔ نمایشی است.
برای مصاحبه و یادگیری معماری است، نه استفادهٔ تولیدی.
مجوز
آموزشی / پورتفولیو. استفاده از نام یا برند بانک‌های ایران نیست.

