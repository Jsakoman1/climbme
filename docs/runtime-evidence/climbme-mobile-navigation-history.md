# Mobile navigation history runtime evidence

Date: 2026-08-13

Viewport: synthetic `390 × 844` browser viewport

Runtime: isolated local Docker Compose package with a fresh synthetic database

Journey: a new synthetic private account completed the existing Log journey,
opened the existing Routes, Training and Insights sections, and then used
browser Back and Forward to restore the immediately preceding and following
sections.

Result: passed. The app preserved the existing private flow and no production
account, climbing record, browser address, secret or tool output is retained in
this evidence record.
