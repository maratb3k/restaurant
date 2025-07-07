export default function CartIcon({ className = "" }: { className?: string }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      className={`h-[24px] w-[24px] ${className}`}
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path d="M8 22C8.55228 22 9 21.5523 9 21C9 20.4477 8.55228 20 8 20C7.44772 20 7 20.4477 7 21C7 21.5523 7.44772 22 8 22Z" strokeWidth="1.75" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M19 22C19.5523 22 20 21.5523 20 21C20 20.4477 19.5523 20 19 20C18.4477 20 18 20.4477 18 21C18 21.5523 18.4477 22 19 22Z" strokeWidth="1.75" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M2.05005 2.05078H4.05005L6.71005 14.4708C6.80763 14.9256 7.06072 15.3323 7.42576 15.6206C7.7908 15.909 8.24495 16.0611 8.71005 16.0508H18.49C18.9452 16.05 19.3865 15.8941 19.7411 15.6086C20.0956 15.3232 20.3422 14.9253 20.4401 14.4808L22.09 7.05078H5.12005" strokeWidth="1.75" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}