import {
    TransitionGroup,
    Transition as ReactTransition,
  } from "react-transition-group"
  const TIMEOUT = 200
  const getTransitionStyles = {
    entering: {
      position: `absolute`,
      opacity: 0,
      transform: `translateX(0px)`,
    },
    entered: {
      transition: `opacity ${TIMEOUT}ms ease-in-out, transform ${TIMEOUT}ms ease-in-out`,
      opacity: 1,
      transform: `translateX(0px)`,
    },
    exiting: {
      transition: `opacity ${TIMEOUT}ms ease-in-out, transform ${TIMEOUT}ms ease-in-out`,
      opacity: 0,
      transform: `translateX(-0px)`,
    },
  }
  const Transition = ({ children, location }) => {
    return (
      <TransitionGroup className={"w-full flex flex-col flex-1"}>
        <ReactTransition
          key={location}
          timeout={{
            enter: TIMEOUT,
            exit: TIMEOUT,
          }}
        >
          {status => (
            <div
            className="flex flex-col flex-1"
              style={{
                ...getTransitionStyles[status],
              }}
            >
              {children}
            </div>
          )}
        </ReactTransition>
      </TransitionGroup>
    )
  }
  export default Transition