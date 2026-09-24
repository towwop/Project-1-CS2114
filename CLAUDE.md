# CS2114 Project 1: Terminal Task Manager

Java 17. Eclipse project rooted at `src/`, sources in `src/src/` (default package).

## Team
- Oscar, Abdullah, Vihaan. All three of us work on every file; no per-file ownership.
- Before editing a file someone else is actively working on, check with them first.
- Tests still need an owner per class, even though code is shared.

## Classes (only these; no extra interfaces or abstract classes)
Main, TUI, TaskStorage, Task, SubTask, StorageFullException.

## Rules
- TaskStorage holds at most 25 tasks (`MAX_TASKS`) in one `Task[]` containing both Tasks and SubTasks.
- `add` / `addSubTask` throw `StorageFullException` (extends `Exception`) when full.
- TaskStorage assigns ids. Ids are never reused.
- Dates use `java.time.LocalDate`. User date input format is `MM/DD/YYYY`, parsed in TUI.
- camelCase fields.
- Input reading, validation, and re-prompting happen only in TUI. Task and SubTask never read input.
  Model classes may reject bad values by returning false/null.
- SubTask extends Task and adds `parentTaskId`. A subtask's parent must exist and cannot be a subtask.
- TUI prints items via `toString()` (overridden in SubTask), never `instanceof`.
- All fields private.

## Testing
Framework: TBD, likely `student.TestCase` (Web-CAT student.jar). Not on the classpath yet. No tests written yet.

## Deliberate deviations from the spec PDF
- `integer` -> `int`; missing return types -> `void`.
- `java.util.Date` -> `LocalDate`; `Task.getDate()` returns `LocalDate`, not `String`.
- Added `Task.getId()` and `Task.toString()` (overridden in SubTask).
- Added `TaskStorage.getSize()` and `getTasks()` so TUI can list tasks.
- `add(Task)` -> `Task add(String, LocalDate)`; added `Task addSubTask(String, LocalDate, int)`.
- `TUI.handleUserAction()` returns `boolean` (false = quit) so Main can exit.
- Added `StorageFullException(String message)` constructor.

## Open decisions
- What happens to subtasks when their parent is removed (cascade, refuse, or orphan).
