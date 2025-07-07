import Logo from "./Logo";
import Navigation from "./Navigation";
import NotificationIcon from "@/icons/Notification";
import CartIcon from "@/icons/Cart";
import ProfileIcon from "@/icons/Profile";

export default function Header({
  isAuthenticated,
  role
}: {
  isAuthenticated: boolean,
  role: string
}) {
  return (
    <header>
      <div className="flex justify-between items-center max-w-[1440px] mx-auto">
        <Logo />
        <Navigation isAuthenticated={isAuthenticated} role={role} />

        <div className="flex gap-[16px]">
          {!isAuthenticated && (
            <button className="btn btn-secondary header-btn">Sign In</button>
          )}

          {isAuthenticated && (
            <>
              {role === "waiter" && (
                <button className="text-[--neutral-900] cursor-pointer hover:text-[var(--green-200)]">
                  <NotificationIcon />
                </button>
              )}
              {role === "user" && (
                <button className="text-[--neutral-900] cursor-pointer hover:text-[var(--green-200)]">
                  <CartIcon />
                </button>
              )}
              <button className="text-[--neutral-900] cursor-pointer hover:text-[var(--green-200)]">
                <ProfileIcon />
              </button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}