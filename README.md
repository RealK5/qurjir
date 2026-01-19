# qurjir

## Pomysł 7: Firma Kurierska — paczki, trasy, statusy

Cel: zarządzanie przesyłkami, punktami nadania/odbioru i trasami kurierów; śledzenie statusów przesyłek.

### Lista kontrolna
1. Setup [02][04]
   - Pakiety: `courier.domain`, `courier.service`, `courier.app`. [04]
   - `courier.app.Main` z `main`. [03]
2. Abstrakcja przesyłki [08]
   - `abstract class Parcel` (id, waga, gabaryt). [08]
   - `StandardParcel`, `FragileParcel` (różne ograniczenia/przetwarzanie). [07]
3. Interfejsy [08]
   - `domain.Trackable` (`start()`, `stop()`, `isActive()`) dla trasy. [08]
   - `domain.Exportable` (historia statusów do CSV). [08]
4. Modele i walidacje [05b][06]
   - `domain.Address`, `domain.Route`, `domain.Status` (enum). [05b]
   - Walidacje pól (niepuste kody, dodatnia waga). [06]
5. Serwisy [03]
   - `service.RoutingService` (przypisywanie paczek do tras). [03]
   - `service.TrackingService` (zmiany statusów). [03]
6. Modyfikatory/kapsułkowanie [05]
   - Prywatne pola, gettery, niemodyfikowalne widoki list statusów. [05]
7. Polimorfizm [07]
   - Różne typy paczek wymagają różnych zasad (np. delikatne z dodatkowymi krokami). [07]
8. Demo [03]
   - Utwórz kilka paczek i tras, zasymuluj doręczenia, wygeneruj CSV. [03]
9. Rozszerzenia (opcjonalnie) [07][08]
   - Strategia wyceny (`PricingStrategy`). [08]
   - `abstract Notifier` (np. email/SMS o statusie). [07]
10. GitHub [02]
    - Repo, `.gitignore`, `README.md` z przykładową historią statusów. [02]
