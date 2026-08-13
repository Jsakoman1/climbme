# ClimbMe mobile Routes and Training runtime evidence

Viewport: synthetic `390 × 844` browser viewport
Runtime: isolated local Docker Compose project `climbme_mobile_ux`
Data: one fresh, synthetic private account, one synthetic attempt and one
synthetic training session

The scenario reaches the phone Route Database from the bottom tab bar, reads the
derived route and invokes the existing bounded Abandon action after accepting
its confirmation. It then reaches Training, records an existing training-session
type and observes that private session in history. This proves presentation and
the already-existing actions only; it does not introduce a route model, training
field, mobile application or public-data behavior.
